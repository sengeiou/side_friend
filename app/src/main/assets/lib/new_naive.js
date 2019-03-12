import Util from "./utilities";
function initNaive(opt) {
    const {baseApi, appId} = opt;
    ServerAgent = {
        invoke: async (api, data) => {
            return await interactCallServer(api, data)
        },
        get: Util.get,
        post: Util.post,
        put: async(api,data)=>{
            return await Util.put(`${baseApi}/${api}`,data);
        },
        load: Util.load,
    };

    //====================================================
    //没有必要存在,功能和ServeAgent一样,
    getAgent = function () {
        return {
            invoke: async (api, data) => await interactCallServer(api, data),
        }
    };

    async function interactCallServer(api, data) {
        let response = await Util.post(`${baseApi}/${appId}/${api}`, data);
        return await interactionTypes[response.verb](response.next, response.args);
    }

    let hibernatingPromise = {};
    let interactionTypes = {
        "redirect": async (next, pck) => {
            let {url} = pck;
            stack.get().open(url, pck);
            return await interactCallServer(next, {});
        },
        "popup": async (next, pck) => {
            let {url} = pck;
            stack.popup(url, pck);
            return await interactCallServer(next, {});
        },
        "retrieve": async (next, pck) => {
            console.log("retrieve",pck);
            let a  = localStorage.get(pck);
            console.log("retrieve",a);
            return await interactCallServer(next,a);
        },
        "info": async (next, pck) => {
            alert(pck);
            return await interactCallServer(next, {});
        },
        "finish": (next, pck) => pck,
        "store": async (next, pck) => {
            localStorage.put(pck);
            return await interactCallServer(next, {});
        },
        "wsconnect": async (next, token) => {
            await webSocket.then(socket=>{
                socket.tokenify(token);
                return socket;
            });
            return await interactCallServer(next,{});
        },
        "input":async(next,pck)=>{
            let {url} = pck;
            let result = await stack.get().open(url, pck);
            return await interactCallServer(next, result);
        },
        "hibernate": (next, pck) => {
            return new Promise((res, rej) => {
                hibernatingPromise[pck.id] = {res, rej};
            }).then(async () => {
                delete hibernatingPromise[pck.id];
                return await interactCallServer(next, {})
            });
        }
    };

    //====================================================
    //强制执行一次,考虑到有hibernate动词存在;
    let webSocket = new Promise((res,rej)=>{
        let socket = io(baseApi);
        let naiveSocket = {
            on:(event,cb)=>{
                socket.on(event,async(pck)=>{
                    let {notify,params} = pck;
                    console.log("socket",pck);
                    let result = cb(params);
                    await interactCallServer(notify,result);
                })
            },
            emit:(event,pck)=>{
                socket.emit(event,pck);
            },
            tokenify:(token)=>{
                let key = "someToken";
                let oldToken = localStorage.get(key,"");
                if (token){
                    if(oldToken !== ""){
                        // socket.emit("disconnect",oldToken);
                    }
                    socket.emit("tokenify",token);
                    localStorage.set({"someToken":token});
                }else {
                    if (oldToken !== ""){
                        socket.emit("tokenify",oldToken);
                    }
                }
            },
        };
        naiveSocket.on("continue",(id)=>{
            console.log("id:"+id);
            if(hibernatingPromise[id])
                hibernatingPromise[id].res();
            return true;
        });
        naiveSocket.on("notification",(message)=>{
            console.log("notification",message);
            Notification.notify(message,message.arguments||{});
            return true;
        });
        naiveSocket.tokenify();
        res(naiveSocket);
    });
    return {webSocket,interactionTypes};
}
export default initNaive;