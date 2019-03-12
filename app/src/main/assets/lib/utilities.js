import sprintf_js from "sprintf-js";
sprintf = sprintf_js.sprintf;
let value = (data) => {
    if (typeof(data) === "string") {
        return data;
    } else if (typeof(data) === "object") {
        return JSON.stringify(data);
    }else {
        return "{}";
    }
};
let param = (data)=>{
    let s = "";
    for(let i in data){
        s+= `${i}=${data[i]}`
    }
    return s;
};
async function post(api,data) {
    let response = await fetch(api, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'text/plain',
            'Accept-Charset': 'UTF-8'
        },
        body: value(data)
    });
    let pck ;
    pck= await response.json();
    console.log("fetch:url",api);
    console.log("fetch:data",data||{});
    console.log("fetch:response",pck);
    if (pck.error) {
        alert(pck.error);
        throw pck.error;
    }
    return pck;
}
async function get(api, data) {
    let response = await fetch(sprintf("%s?%s", api, param(data)));
    let pck = await response.json();
    if (pck.error){
        alert(pck.error);
        throw pck.error;
    }
    return pck;
}
async function put(api,data) {
    let response = await fetch(api,{
        method:"put",
        body: data,
    });
    let pck = await response.json();
    if (pck.error){
        alert(pck.error);
        throw pck.error;
    }
    return pck;
}
async function load(api) {
    let response = await fetch(api);
    let file;
    try {
        file = await response.blob();
    }catch (e){
        throw "";
    }
    return file;
}

export default {post,get,put,load};