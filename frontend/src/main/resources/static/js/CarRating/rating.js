let stars = document.querySelectorAll(".ratings span");

for(let star of stars){
    star.addEventListener("click", function(){

        console.log("star clicked")
        let children = 	star.parentElement.children;
        for(let child of children){
            child.removeAttribute("data-clicked")
        }

        this.setAttribute("data-clicked","true");
        let rating = this.dataset.rating;
        sessionStorage.setItem("rating", rating);
    });
}