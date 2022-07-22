let me = new Vue({
    el:"#me",
    data:{
        userVO:{}
    },
    created:function () {
        axios.get("http://localhost:9002/v1/users/me").then(function (r) {
            me.userVO = r.data;
        })
    }
})