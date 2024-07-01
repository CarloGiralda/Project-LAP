document.addEventListener("DOMContentLoaded", async function () {
    document.getElementById("auctionButton").addEventListener("click",
        async function () {
            sessionStorage.setItem("isAuction", "1");
            window.location.href = "/carinsert/insertauction";
        })

    document.getElementById("carButton").addEventListener("click",
        async function () {
            sessionStorage.setItem("isAuction", "0");
            window.location.href = "/carinsert/insertoffer";
        })
})