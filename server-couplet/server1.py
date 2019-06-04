from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import urllib
import requests
import time
from datetime import datetime
from flask import Flask, jsonify, request
from flask_cors import CORS, cross_origin
from model import Model
#from gevent.wsgi import WSGIServer
import logging

app = Flask(__name__)
CORS(app)

vocab_file = 'vocabs'
model_dir = './doubi'

data = {'result': 'this is a test'}
host = ('10.21.184.37', 8888)

#加载模型
m = Model(
        None, None, None, None, vocab_file,
        num_units=1024, layers=4, dropout=0.2,
        batch_size=32, learning_rate=0.0001,
        output_dir=model_dir,
        restore_model=True, init_train=False, init_infer=True)

#通过模型对对联
def chat_couplet(in_str):
    if len(in_str) == 0 or len(in_str) > 50:
        output = u'您的输入太长了'
    else:
        output = m.infer(' '.join(in_str))
        output = ''.join(output.split(' '))
    print('上联：%s；下联：%s' % (in_str, output))
    return output



class Resquest(BaseHTTPRequestHandler):
    def do_POST(self):
        length = int(self.headers['Content-Length'])
        post_data = urllib.parse.parse_qs(self.rfile.read(length).decode('utf-8'))

        print(post_data)

        out_couplet = chat_couplet(post_data['couplet'][0])
        
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(json.dumps({'next_couplet':out_couplet},ensure_ascii=False).encode())
                
    def do_GET(self):
        now_time = datetime.now()
        global ori_time
        #如果距离上次请求超过了一天，则更新推荐列表
        p = now_time - ori_time
        if p.days >= 1:
            ori_time = now_time
            get_recommendation() 

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(json.dumps({'data':recommendations},ensure_ascii=False).encode())

def get_recommendation():
    #首先获取十个推送
    re_url = 'https://api.gushi.ci/all.json'
    if len(recommendations) == 0:
        for i in range(10):
            r = requests.get(re_url)
            time.sleep(0.2)
            #格式化
            print(r.text.replace('\n','').replace(' ',''))
            recommendation = json.loads(r.text.replace('\n','').replace(' ',''))
            recommendations.append(recommendation)

    else:
        try:
            r = requests.get(re_url,timeout=3)
        #使所有推荐往后移位
            for i in range(9):
                recommendations[-i - 1] =recommendations[-i - 2] 

            #格式化
            print(r.text.replace('\n','').replace(' ',''))
            recommendation = json.loads(r.text.replace('\n','').replace(' ',''))
            recommendations[0] = recommendation

        #超时异常
        except:
            print('TimeoutError')


    

if __name__ == '__main__':
    global recommendations
    recommendations = []

    #声明全局变量
    global ori_time
    ori_time = datetime.now()

    get_recommendation()

    server = HTTPServer(host, Resquest)
    print("Starting server, listen at: %s:%s" % host)
    server.serve_forever()