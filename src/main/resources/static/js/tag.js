const input_tag = document.querySelector('.input-tag')
const tags_length = document.querySelector('.tags-length')
let tags_list =[]

input_tag.addEventListener("keyup", (e) => {
    const val = input_tag.value
    if (e.key == "Enter") {
        if (tags_list.some(e => e.text == val)) return alert('Duplicate Tag')
        if (val == "") return

        const tags = val.split(',').map(e => e.trim()).filter(e => e !== "")

        for (let i of tags) {
            tags_list.push({text: i})
        }
        input_tag.value = ""
        RenderTags()
    }
})

function RenderTags (){
    const wrapper_tags = document.querySelector(".wrapper-tags")
    let cache = ""

    document.querySelectorAll(".item-tag").forEach(e => e.remove())
    cache = ""

    tags_list.forEach(e => {
        cache = `<div class="item-tag">
                    <span>${e.text}</span>
                    <button type="button" data-id="${e.id}" class="btn-rm-tag">
                        <i class="fas fa-times"></i>
                    </button>
                 </div>`;
        wrapper_tags.insertAdjacentHTML('afterbegin', cache)
    })
    tags_length.textContent = `${tags_list.length} Tags`
}

// function HandleRmTags(){
//     const btns = document.
// }