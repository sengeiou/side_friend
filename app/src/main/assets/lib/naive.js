import Util from "./utilities";
function initNaive(opt) {
    const {baseApi,uploadApi} = opt;
    ServerAgent = {
        invoke: async (api, data) => {
            return await interactCallServer("logic",api, data)
        },
        get: Util.get,
        post: Util.post,
        put: async(api,data)=>{
            return await Util.put(`${uploadApi}/${api}`,data);
        },
        load: Util.load,
    };
    getAgent = function (naive) {
        return {
            invoke: async (method, data) => await interactCallServer(`naive/${naive}`,`invoke/${method}`, data),
        }
    };

    async function interactCallServer(type,api, data) {
        let response = await Util.post(`${baseApi}/${type}/${api}`, data);
        return await interactionTypes[response.verb](response.next, response.args);
    }

    let interactionTypes = {
        "redirect": async (next, pck) => {
            let {url} = pck;
            stack.get().open(url, pck);
            return await interactCallServer("interact",next, {});
        },
        "popup": async (next, pck) => {
            let {url} = pck;
            stack.get().popup(url, pck);
            return await interactCallServer("interact",next, {});
        },
        "retrieve": async (next, pck) => {
            let a  = localStorage.get(pck);
            return await interactCallServer("interact",next,a);
        },
        "info": async (next, pck) => {
            alert(pck);
            return await interactCallServer("interact",next, {});
        },
        "finish": (next, pck) => pck,
        "store": async (next, pck) => {
            localStorage.put(pck);
            return await interactCallServer("interact",next, {});
        },
        "wsconnect": async (next, token) => {
            Socket.emit("tokenify",token);
            return await interactCallServer("interact",next,{});
        },
        "input":async(next,pck)=>{
            let {url} = pck;
            let result = await stack.get().open(url, pck);
            return await interactCallServer("interact",next, result);
        },
    };

    return {interactionTypes};
}
export default initNaive;