package com.example.nd_excel.Util;

import org.json.JSONObject;

public class JsonConCap {
    public static JSONObject cacuJson=null;
    public static JSONObject imageJson=null;
    public static JSONObject menuJson=null;
    public static JSONObject q_a_Json=null;
    public static JSONObject presayJson=null;
    public static JSONObject seqJson=null;
    public static JSONObject sharedStringJson=null;
    public static JSONObject xmlChapter_1_Json=null;
    public static JSONObject xmlChapter_2_Json=null;
    public static JSONObject hyp_1_Json=null;
    public static JSONObject hyp_2_Json=null;
    public static JSONObject imageNameJson=null;
    public static JSONObject textSeq_1_Json=null;
    public static JSONObject imageSeqJson=null;
    public static JSONObject textSeq_2_Json=null;

    public static JSONObject getCacuJson() {
        return cacuJson;
    }

    public static void setCacuJson(JSONObject cacuJson) {
        JsonConCap.cacuJson = cacuJson;
    }

    public static JSONObject getImageJson() {
        return imageJson;
    }

    public static void setImageJson(JSONObject imageJson) {
        JsonConCap.imageJson = imageJson;
    }

    public static JSONObject getMenuJson() {
        return menuJson;
    }

    public static void setMenuJson(JSONObject menuJson) {
        JsonConCap.menuJson = menuJson;
    }

    public static JSONObject getQ_a_Json() {
        return q_a_Json;
    }

    public static void setQ_a_Json(JSONObject q_a_Json) {
        JsonConCap.q_a_Json = q_a_Json;
    }

    public static JSONObject getPresayJson() {
        return presayJson;
    }

    public static void setPresayJson(JSONObject presayJson) {
        JsonConCap.presayJson = presayJson;
    }

    public static JSONObject getSeqJson() {
        return seqJson;
    }

    public static void setSeqJson(JSONObject seqJson) {
        JsonConCap.seqJson = seqJson;
    }

    public static JSONObject getSharedStringJson() {
        return sharedStringJson;
    }

    public static void setSharedStringJson(JSONObject sharedStringJson) {
        JsonConCap.sharedStringJson = sharedStringJson;
    }

    public static JSONObject getXmlChapter_1_Json() {
        return xmlChapter_1_Json;
    }

    public static void setXmlChapter_1_Json(JSONObject xmlChapter_1_Json) {
        JsonConCap.xmlChapter_1_Json = xmlChapter_1_Json;
    }

    public static JSONObject getXmlChapter_2_Json() {
        return xmlChapter_2_Json;
    }

    public static void setXmlChapter_2_Json(JSONObject xmlChapter_2_Json) {
        JsonConCap.xmlChapter_2_Json = xmlChapter_2_Json;
    }

    public static JSONObject getHyp_1_Json() {
        return hyp_1_Json;
    }

    public static void setHyp_1_Json(JSONObject hyp_1_Json) {
        JsonConCap.hyp_1_Json = hyp_1_Json;
    }

    public static JSONObject getHyp_2_Json() {
        return hyp_2_Json;
    }

    public static void setHyp_2_Json(JSONObject hyp_2_Json) {
        JsonConCap.hyp_2_Json = hyp_2_Json;
    }

    public static JSONObject getImageNameJson() {
        return imageNameJson;
    }

    public static void setImageNameJson(JSONObject imageNameJson) {
        JsonConCap.imageNameJson = imageNameJson;
    }

    /*
     "cacu_1.json",
            "image_1.json",
            "menu_1.json",
            "q_a_1.json",
            "pre_say_1.json",
            "sequential_aeeangement_1.json",
            "shared_string_1.json",
            "xml_chapter_1.json",
            "xml_chapter_2.json",
            "hyp.json",
            "hyp_1.json",
            "image_name_1.json"//index 11
             "text_seq.json",
            "image_sep.json",
            "text_seq_1.json"//index 14
     */

    public static JsonConCap jsonConCap=null;
    public static JsonConCap getInstance(){
        if (jsonConCap==null){
            jsonConCap=new JsonConCap();
            makeAllJson();
        }
        return jsonConCap;
    }
    public static void makeAllJson(){
        makeCacuJson();
        makeImageJson();
        makeMenuJson();
        makeQAJson();
        makePresayJson();
        makeSeqJson();
        makeSharedStringJson();
        makeXmlChapter_1_Json();
        makeXmlChapter_2_Json();
        makeHyp_1_Json();
        makeHyp_2_Json();
        makeImageNameJson();
        makeTestSeq_2_Json();
        makeImageSeqJson();
        makeTestSeq_1_Json();
    }

    public static JSONObject getTextSeq_1_Json() {
        return textSeq_1_Json;
    }

    public static void setTextSeq_1_Json(JSONObject textSeq_1_Json) {
        JsonConCap.textSeq_1_Json = textSeq_1_Json;
    }

    public static JSONObject getImageSeqJson() {
        return imageSeqJson;
    }

    public static void setImageSeqJson(JSONObject imageSeqJson) {
        JsonConCap.imageSeqJson = imageSeqJson;
    }

    public static JSONObject getTextSeq_2_Json() {
        return textSeq_2_Json;
    }

    public static void setTextSeq_2_Json(JSONObject textSeq_2_Json) {
        JsonConCap.textSeq_2_Json = textSeq_2_Json;
    }

    //cacuJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[0]);
    public static void makeCacuJson(){
        cacuJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[0]);
    }

    public static void makeImageJson(){
        imageJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[1]);
    }

    public static void makeMenuJson(){
        menuJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[2]);
    }

    public static void makeQAJson(){
        //cacuJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[2]);
    }

    public static void makePresayJson(){
        presayJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[4]);
    }

    public static void makeSeqJson(){
        //seqJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[5]);
    }

    public static void makeSharedStringJson(){
        sharedStringJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[6]);
    }

    public static void makeXmlChapter_1_Json(){
        xmlChapter_1_Json=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[7]);
    }

    public static void makeXmlChapter_2_Json(){
        xmlChapter_2_Json=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[8]);
    }

    public static void makeHyp_1_Json(){
        hyp_1_Json=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[9]);
    }

    public static void makeHyp_2_Json(){
        hyp_2_Json=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[10]);
    }

    public static void makeImageNameJson(){
        imageNameJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[11]);
    }

    public static void makeTestSeq_1_Json(){
        textSeq_1_Json=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[12]);
    }

    public static void makeImageSeqJson(){
        imageSeqJson=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[13]);
    }

    public static void makeTestSeq_2_Json(){
        textSeq_2_Json=TestUtil.readJsonInAsset("datas/"+TestUtil.jsonFileNames[14]);
    }

}
