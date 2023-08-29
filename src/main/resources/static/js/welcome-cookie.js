function GetCookie(name) {
    const arg = name + "=";
    const alen = arg.length;
    const clen = document.cookie.length;
    let i = 0;
    while (i<clen) {
        const j = i + alen;
        if (document.cookie.substring(i,j)===arg)
            return "here";
        i=document.cookie.indexOf(" ",i)+1;
        if (i===0) break;
    }
    return null;
}
function testFirstCookie() {
    const visit = GetCookie("FirstTimeVisitCookie");
    if (visit == null) {
        let expire = new Date();
        expire = new Date(expire.getTime() + 7776000000);
        document.cookie = "FirstTimeVisitCookie=here; expires=" + expire + ";path=/";

        $('#firstTimeVisitorModal').modal('show');
    }
}

$(document).ready(function(){
    testFirstCookie();
});