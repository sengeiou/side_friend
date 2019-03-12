document.senderEvaluate = {
    isOnline:false,
    isMessageMatch:false,
    content:'',
    isSatisfyService:false,
    isTimeout:false,
    isGratuity:0,
    additionalMessage:{
        text:'',
        audio:[],
        video:[],
        image:[],
    },
};
document.changeStatus = (id)=>{
    document.senderEvaluate[id] = !document.senderEvaluate[id];
    document.$(id).get('rimg').set({src:document.senderEvaluate[id]?'img/task/choice_on.png':'img/task/choice_off.png'});
    if(document.senderEvaluate[id]){
        switch(id){
            case "isSatisfyService":
                document.$('ratingbar').minus({nums:document.$('ratingbar').val()+'',setnum:'20',flag:'0'});
                document.$('label3').show(true);
                break;
            case "isTimeout":
                document.$('label2').show(true);
                document.$('ratingbar').minus({nums:document.$('ratingbar').val()+'',setnum:'40',flag:'1'});
                break;
            case "isMessageMatch":
                document.$('label1').show(true);
                document.$('ratingbar').minus({nums:document.$('ratingbar').val()+'',setnum:'40',flag:'1'});
                break;
            case "isOnline":
                break;
        }
    }else{
        switch(id){
            case "isSatisfyService":
            document.$('ratingbar').add({nums:document.$('ratingbar').val()+'',setnum:'20',flag:'0'});
            document.$('label3').show(false);
            document.$('label3').refresh({init:'init'});
                break;
            case "isTimeout":
                document.$('label2').show(false);
                document.$('label2').refresh({init:'init'});
                document.$('ratingbar').add({nums:document.$('ratingbar').val()+'',setnum:'40',flag:'1'});
                break;
            case "isMessageMatch":
                document.$('label1').show(false);
                document.$('label1').refresh({init:'init'});
                document.$('ratingbar').add({nums:document.$('ratingbar').val()+'',setnum:'40',flag:'1'});
                break;
            case "isOnline":
                break;
        }
    }
};
document.getEvaluateData = ()=>{
    let str =  document.$('label1').val().content+document.$('label2').val().content+document.$('label3').val().content;
    let str2 = str.slice(0, str.length-1);
    document.senderEvaluate.additionalMessage.text = str2;
    let item = {
        taskId:document.arguments().taskId,
        isMessageMatch:!document.senderEvaluate.isMessageMatch,
        content:document.$('input').val(),
        isSatisfyService:!document.senderEvaluate.isSatisfyService,
        isTimeout:document.senderEvaluate.isTimeout,
        isGratuity:document.senderEvaluate.isGratuity,
        additionalMessage:document.senderEvaluate.additionalMessage,
        isOnline:document.senderEvaluate.isOnline,
    };
    return item;
};
document.$('label1').set({text:'与照片不符,性别不符,用户信息不符'});
document.$('label2').set({text:'与照片,性别不符,用符'});
document.$('label3').set({text:'与照片符,性不符,用户信不符'});
document.$('label1').show(false);
document.$('label2').show(false);
document.$('label3').show(false);

document.$('isOnline').show(!!document.arguments().obj.advancePayIsNeedPay);
