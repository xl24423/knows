let answerApp = new Vue({
        el: '#answerApp',
        data: {
            init: false,
            answers: [],
            answerNum: "",
            editPermission: {},
            commentEditPermission: {},
        },
        methods: {
            async loadAllAnswer() {
                var that = this;
                await axios.get("http://localhost:9002/v1/answer/getAnswersByQuestionId?id=" + id).then(function (r) {
                    if (r.status === 200) {
                        if (r.data === "") {
                            that.answers = null;
                        } else {
                            that.answers = r.data;
                            that.updateDuration();
                            axios({
                                url: "/v1/answer/editPermissionByNowUser",
                                method: "GET",
                                params: {
                                    id: id,
                                },
                            }).then(function (r) {
                                that.editPermission = r.data;
                                axios.get("/v1/comment/commentEditPermissionByNowUser?id=" + id).then(function (r) {
                                    answerApp.commentEditPermission = r.data;
                                    let answers = that.answers;
                                    let editPermission = that.editPermission;
                                    for (let i = 0; i < answers.length; i++) {
                                        answers[i].show = editPermission[answers[i].id];
                                        let comments = answers[i].comments;
                                        let commentEditPermission = that.commentEditPermission;
                                        for (let i = 0; i < comments.length; i++) {
                                            comments[i].show = commentEditPermission[comments[i].id];
                                            console.log(comments[i].show)
                                        }
                                    }
                                    that.init = true;
                                })
                            })
                            that.loadAnswerNum();
                        }
                    } else {
                        alert("服务异常,获取回答失败")
                        location.href = "#";
                    }
                })
            },
            updateDuration: function () {
                let answers = this.answers;
                for (let i = 0; i < answers.length; i++) {
                    addDuration(answers[i]);
                    let comments = answers[i].comments;
                    for (let i = 0; i < comments.length; i++) {
                        addDuration(comments[i]);
                    }
                }
            },
            loadAnswerNum: function () {
                axios({
                    url: "/v1/answer/getAnswerNum",
                    method: "GET",
                    params: {
                        id: id,
                    }
                }).then(function (r) {
                    if (isNaN(r.data)) {
                        answerApp.answerNum = r.data;
                    } else {
                        answerApp.answerNum = r.data + "条回答";
                    }
                })
            },


            deleteAnswer: function (id) {
                axios.delete("/v1/answer/deleteAnswer?id=" + id).then(function (r) {
                    if (r.status === 200) {
                        if (r.data === 1) {
                            alert("删除成功");
                            location.reload();
                        } else {
                            alert(r.data);
                        }
                    } else {
                        alert("请求失败,请稍后再试")
                    }
                })
            },
            postComment: function (answerId) {
                // 新增评论功能,需要 answerId 和 content         ↓↓ textarea前面的空格千万别删
                let textarea = $('#collapseExample' + answerId + " textarea");
                let content = textarea.val();
                // 创建表单
                let form = new FormData();
                form.append("answerId", answerId);
                form.append("content", content);
                axios.post("/v1/comment", form).then(function (r) {
                    if (r.status === 200) {
                        if (r.data == "") {
                            alert("评论失败")
                        } else {
                            location.reload();
                        }
                    } else {
                        alert(r.data + ",评论失败");
                    }
                })
            },
            deleteComment: function (id) {
                if (confirm("请确定要删除吗")) {
                    axios.delete("/v1/comment/deleteByCommentId?id=" + id).then(function (r) {
                        if (r.status === 200) {
                            if (r.data === 1) {
                                location.reload();
                            } else {
                                alert(r.data)
                            }
                        } else {
                            alert("服务器异常,删除失败")
                        }
                    })
                }
            },
            updateComment: function (commentId, index, answer) {
                // 新增评论功能,需要 answerId 和 content         ↓↓ textarea前面的空格千万别删
                let textarea = $("#editComment" + commentId + " textarea");
                let content = textarea.val();
                if (!content) {
                    return;
                }
                let form = new FormData();
                form.append("answerId", answer.id);
                form.append("content", content);
                axios({
                    url: "/v1/comment/update/" + commentId,
                    method: "post",
                    data: form
                }).then(function (r) {
                    if (typeof (r.data) == "object") {
                        // Vue更新数组的方法,这里我因为对每个元素要进行权限搜索,所以要刷页面
                        // Vue.set(answer.comments, index, r.data);
                        // $("#editComment"+commentId).collapse("hide");
                        location.reload();
                    }else{
                        alert("失败,"+r.data);
                    }
                })
            },
            answerSolved:function (answerId) {
                axios.get("/v1/answer/"+answerId+"/solved").then(function (r){
                    alert(r.data);
                })
            }
        },


        created: function () {
            this.loadAllAnswer();
        }
    }
)

let postAnswerApp = new Vue({
    el: '#postAnswerApp',
    data: {
        permission: false,
    },
    methods: {
        postAnswer: function () {
            let qid = id;
            if (!id || isNaN(id)) {
                alert("地址有误,请重试")
                location.href = "/";
            } else {
                let form = new FormData();
                let content = $('#summernote').val();
                form.append("questionId", qid);
                form.append("content", content);
                axios.post("/v1/answer", form).then(function (r) {
                    console.log(r.data);
                    let answer = r.data;
                    answer.duration = "刚刚";
                    answer.show = true;
                    answerApp.answers.push(answer);
                    $("#summernote").summernote("reset");

                })
            }
        }
    },
    mounted: function () {
        axios.get("/v1/permission/hasAnswerPermission").then(function (r) {
            postAnswerApp.permission = r.data;
        })
    }

})