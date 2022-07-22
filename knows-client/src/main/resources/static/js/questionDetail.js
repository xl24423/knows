let id = location.href.split("=")[1];
    let detail = new Vue({
    el:'#detail',
    data:{
    question:{},
    duration:"",
},
    methods:{
    loadQuestionDetail:function(){
        if (!id || isNaN(id)){
            alert("地址有误,请重试")
            location.href="/";
        }else{
            axios.get("http://localhost:9002/v2/questions/questionDetail?id="+id).then(function(r){
                if (r.status !== 200 || r.data===""){
                    alert("获取失败");
                    location.href="/";
                }else{
                    detail.question = r.data;
                    addDuration(r.data)
                }
            })
        }
}

},
    created:function(){
    this.loadQuestionDetail();
}
})
let count = detail.question.replyCount;

