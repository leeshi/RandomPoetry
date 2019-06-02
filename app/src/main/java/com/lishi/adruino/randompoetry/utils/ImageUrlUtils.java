package com.lishi.adruino.randompoetry.utils;

public class ImageUrlUtils {
    private final static String[] URLS = new String[]{
            "https://i.ytimg.com/vi/z9Y-62hV-rM/maxresdefault.jpg",
            "https://watermark.lovepik.com/photo/50066/0458.jpg_wh1200.jpg",
            "http://www.51pptmoban.com/d/file/2017/10/08/cfca38134233c8a46d8de466ccd8d5f5.jpg",
            "http://pic2bj.shejibao.com/img/2016/08/23/9a28bea3d35ad6f003dda820350019d6.jpg@!width_800",
            "http://upload.art.ifeng.com/2018/0621/1529553560153.jpg",
            "http://pic.90sjimg.com/back_pic/qk/back_origin_pic/00/03/07/c6ca91c386bd2b5f76c03892bdaac012.jpg",
            "https://cdn.pixabay.com/photo/2014/10/29/07/32/ink-507639_960_720.jpg",
            "http://img1.juimg.com/171121/330849-1G12112114221.jpg",
            "http://www.minghui.org/mh/article_images/2010-5-14-huaguoyuan-04.jpg",
            "http://file.yuansuxi.com/n002/362g_700x351.jpg",
            "https://up.enterdesk.com/edpic_source/33/c1/b3/33c1b311f099ce334a2e8485a4a216c8.jpg",
            "http://pic.616pic.com/bg_w1180/00/17/37/dSV6wtofEd.jpg",
            "https://up.enterdesk.com/edpic_source/6d/19/4f/6d194fe31243ead8c5ac29db12e98bec.jpg",
            "http://img1.gtimg.com/rushidao/pics/hv1/161/168/1534/99791351.jpg",
            "http://ku.90sjimg.com/element_origin_min_pic/19/03/20/1656ed13920c662.jpg",
            "http://5b0988e595225.cdn.sohucs.com/images/20190321/2e13de5d07a14e828ae24bd104a982b3.jpeg",
            "http://image.huahuibk.com/uploads/20181227/15/1545897114-btwlshLduE.jpg",
            "http://218.16.125.44/img/20180825/131796682248549223.jpg",
            "https://bpic.588ku.com/original_origin_min_pic/18/08/30/66130c2e0a6713613ef767fc5a348e54.jpg",
            "http://upload.art.ifeng.com/2018/0621/1529553560153.jpg",
            "http://file.763g.com/tu/d/file/p/2015-05-21/pyollcav1m59446.jpg",
            "https://images.shobserver.com/news/690_390/2017/7/14/c09bd564-ae0f-4c8b-90fe-06ece42c9486.jpg",
            "http://img01.1sucai.com/170618/330772-1F61QFU045.jpg"
    };
    public static String[] getUrls(){
        String[] result = new String[4];

        int n = URLS.length;
        int[] n1 = new int[n + 2];
        n1[n + 1]++;
        for(int i = 0;i < 4;i++){
            int index = n + 1;
            while(n1[index] != 0){
                index = (int)(Math.random() * n);
                System.out.println(index);
            }
            n1[index]++;
            result[i] = URLS[index];
        }

        return result;
    }
}
