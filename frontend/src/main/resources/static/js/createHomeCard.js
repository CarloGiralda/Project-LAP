function createCard(title, content, href, image) {
    const carouselItem = document.createElement("carousel-item")
    const card = document.createElement('div')
    const cardBody = document.createElement('div')

    card.className = 'card m-2';
    cardBody.className = 'card-body'

    card.style.width = '18rem';

    const cardTitle = document.createElement('h5');
    cardTitle.className = 'card-title';
    cardTitle.textContent = title;

    const cardContent = document.createElement('div');
    cardContent.className = 'card-content';

    // Iterate through the content array and create a <p> element for each line
    content.forEach(line => {
        const cardText = document.createElement('p');
        cardText.className = 'card-text';
        cardText.textContent = line;
        cardContent.appendChild(cardText);
    });

    const cardHref = document.createElement('div')


    href.forEach(line => {

        const cardLink = document.createElement('a');
        cardLink.className = 'd-inline-flex align-items-center btn btn-primary btn-lg px-4 rounded-pill mt-2 me-2';
        cardLink.href = line
        if (line.includes('extend')){
            cardLink.textContent = 'Extend';
        }
        if (line.includes('bookings')) {
            cardLink.textContent = 'Car page'
        }
        if (line.includes('carsearch')) {
            cardLink.textContent = 'Car details'
        }
        if (line.includes('carinsert')) {
            cardLink.textContent = 'Modify your car'
        }
        if (line.includes("deleteSub")){
            cardLink.className = 'btn btn-outline-danger btn-lg px-4 rounded-pill mt-2 me-2';
            cardLink.textContent = 'Unsubscribe'
            const temp = line.split('/')
            const cid = temp[1]
            // Set data-index to identify car
            cardLink.setAttribute('data-index', cid);
            cardLink.href = "#";
        }
        if (line.includes("deleteZoneSub")){
            cardLink.className = 'btn btn-outline-danger btn-lg px-4 rounded-pill mt-2 me-2';
            cardLink.textContent = 'Unsubscribe'

            const temp = line.split('/')
            const zoneId = temp[temp.length - 1]

            cardLink.setAttribute('data-index', zoneId);
            cardLink.href = "#";
        }

        cardHref.appendChild(cardLink)

    })

    if (image != null){

        const imageElement = document.createElement("img");
        imageElement.className = "card-img-top img-fluid"

        imageElement.alt = "car image "

        if (typeof image === "object") {
            const blob = new Blob([new Uint8Array(image)], { type: 'image/jpeg' });

            imageElement.src = URL.createObjectURL( blob )
        } else {
            /*const convertedImage = base64ToByteArray(image)
            console.log("converted image:", convertedImage)*/
            imageElement.src = "data:image/jpeg;base64," + image;
        }

        card.appendChild(imageElement)
    }

    cardBody.appendChild(cardTitle)
    cardBody.appendChild(cardContent)
    cardBody.appendChild(cardHref)
    card.appendChild(cardBody)
    carouselItem.append(card)

    return carouselItem;
}
