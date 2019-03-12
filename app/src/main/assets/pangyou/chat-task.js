let oldY = 0;
let currentStatus;
let STATE_RECORDING = 1;
let STATE_WANT_TO_CANCEL = 2;
let isRecording = false;
let record = new Record("pangyou");
let audioImgMap = {
    1:"img/audio/yuyin_voice_1.png",
    2:"img/audio/yuyin_voice_2.png",
    3:"img/audio/yuyin_voice_3.png",
    4:"img/audio/yuyin_voice_4.png",
    5:"img/audio/yuyin_voice_5.png",
};

document.chanceImg = (level)=>{
    setTimeout(()=>{
        if(isRecording){
            if(currentStatus === STATE_RECORDING){
                if(level <= 0 || level >= 6){
                    level = 5;
                }
                document.dialog().$('audioImg').set({src:audioImgMap[level]});
            }
        }
        document.chanceImg(parseInt(Math.random() * 8));
    },400);
};

document.changeState = (state)=>{
    if(currentStatus !== state){
        currentStatus = state;
        switch(currentStatus){
            case STATE_RECORDING:
                document.dialog().$('audioText').get('cp').set({p:'手指上划,取消发送'});
                document.chanceImg(parseInt(Math.random() * 8));
                break;
            case STATE_WANT_TO_CANCEL:
                document.dialog().$('audioText').get('cp').set({p:'松开手指,取消发送'});
                document.dialog().$('audioImg').set({src:'img/audio/yuyin_cancel.png'});
                break;
        }
    }
};

document.$('chat').get('limg',1).touch((event)=>{
    switch(event.action){
        case event.ACTION_DOWN:
            oldY = event.y;
            document.dialog('pangyou/chat-audio-dialog.xml');
            isRecording = true;
            record.prepare();
            break;
        case event.ACTION_MOVE:
            if(oldY - event.y >= 300){
                document.changeState(STATE_WANT_TO_CANCEL);
            }else{
                document.changeState(STATE_RECORDING);
            }
            break;
        case event.ACTION_UP:
            document.dialog().next();
            isRecording = false;
            record.stop();
            if(STATE_RECORDING === currentStatus){
                let url = record.getUrl();
                let formData = new FormData();
                formData.append('amr',url);
                ServerAgent.put('uploadFiles',formData).then(urls=>{
                    ServerAgent.invoke('chat-send',{houseId:document.arguments().houseId,content:JSON.stringify({url:urls[0].url,second:2}),type:'audio'}).then(res=>{
                        console.log('发送成功',res);
                        document.adapter().load({
                            self:localStorage.get('uid',0),
                            sender:localStorage.get('uid',0),
                            content:JSON.stringify({url:urls[0].url,second:2}),
                            type:'audio',
                            senderImg:localStorage.get('img'),
                            senderName:localStorage.get('nickname'),
                        });
                        document.scroll(-1);
                    });
                });
            }else{
                alert('取消发送');
            }
            break;
    }
});





let face = document.$("face");
let more = document.$("more");
face.show(false);
more.show(false);

let isFaceShow = false,
    isAudioShow = false,
    isImmShow = false,
    isMoreShow = false;
document.keyword.showListener = (height)=>{
    console.log('keyword show改变高度',height+'');
    isImmShow = true;
};
document.keyword.hideListener=(height)=>{
    console.log('keyword hide改变高度',height+'');
    isImmShow = false;
};

let showStatus = ()=>{
    console.log("isFaceShow",isFaceShow);
    console.log("isAudioShow",isAudioShow);
    console.log("isImmShow",isImmShow);
    console.log("isMoreShow",isMoreShow);
}
document.faceClick=()=>{
    if(isFaceShow){
        document.keyword.nothing().show();
        isImmShow = true;
        setTimeout(()=>{
            face.show(false);
            document.keyword.resize();
            document.scroll(-1);
        },65);
    }else {
        if(isImmShow){
            document.keyword.nothing().hide();
            isImmShow = false;
            setTimeout(()=>{
                face.show();
                document.keyword.resize();
                document.scroll(-1);
            },65);
        }else if(isMoreShow){
            more.show(false);
            face.show();
            isMoreShow = false;
        }else if (isAudioShow){
            face.show();
            isAudioShow = false;
        }else {
            face.show();
        }
    }
    isFaceShow = !isFaceShow;
    showStatus();
    console.log("faceClick");
};
document.audioClick=()=>{

};
document.moreClick=()=>{
    let text = document.$('chat').val();
    if(text.length > 0){
        document.$('chat').get('rimg',2).set({src:'img.add.png'});
        ServerAgent.invoke('chat-send',{houseId:document.arguments().houseId,content:text,type:'text'}).then(res=>{
            console.log('发送成功',res);
            document.adapter().load({
                self:localStorage.get('uid',0),
                sender:localStorage.get('uid',0),
                content:text,
                type:'text',
                senderImg:localStorage.get('img'),
                senderName:localStorage.get('nickname'),
            });
            document.scroll(-1);
        });
        document.$('chat').get('input').set({p:''});
        return;
    }
    if(isMoreShow){
        document.keyword.nothing().show();
        isImmShow = true;
        setTimeout(()=>{
            more.show(false);
            document.keyword.resize();
            document.scroll(-1);
        },65);
    }else {
        if(isImmShow){
            document.keyword.nothing().hide();
            more.show();
            isImmShow = false;
        }else if(isFaceShow){
            face.show(false);
            more.show();
            isFaceShow = false;
        }else if (isAudioShow){
            more.show();
            isAudioShow = false;
        }else {
            more.show();
        }
    }
    isMoreShow = !isMoreShow;
    showStatus();
    console.log("moreClick");
};
document.inputClick=()=>{
    if(isFaceShow){
        document.keyword.nothing().show(true);
        isFaceShow = false;
        setTimeout(()=>{
            face.show(false);
            document.keyword.resize();
            document.scroll(-1);
        },65);
    }else if(isMoreShow){
        document.keyword.nothing().show(true);
        isMoreShow = false;
        setTimeout(()=>{
            more.show(false);
            document.keyword.resize();
            document.scroll(-1);
        },65);
    }else{
        document.keyword.show(true).resize();
        document.scroll(-1);
    }
    isImmShow = true;
    showStatus();
    console.log("inputClick");
};
document.onBack = ()=>{
    showStatus();
    if(isFaceShow||isMoreShow){
        document.$('chat').get('rimg',1).set({src:'img.smile.png'});
        face.show(false);
        isFaceShow = false;
        more.show(false);
        isMoreShow = false;
        return false;
    }
    return true;
};