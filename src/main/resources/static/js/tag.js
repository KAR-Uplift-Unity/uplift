const input_tag = document.querySelector('.input-tag')
const tags_length = document.querySelector('.tags-length')
let tags_list =[]

input_tag.addEventListener("keydown", (e) => {
    const val = input_tag.value
    if (e.key == "Shift") {
        if (tags_list.some(e => e.text == val)) return alert('Duplicate Tag')
        if (val == "") return;

        const tags = val.split(',').map(e => e.trim()).filter(e => e !== "")

        for (let i of tags) {
            tags_list.push({
                id: Math.random().toString(10).substring(2,10),
                text: i,
            })
        }
        input_tag.value = ""
        console.log(tags_list)
        RenderTags()
    }
})

function RenderTags (){
    const wrapper_tags = document.querySelector(".wrapper-tags")
    const tagString = document.getElementById("tagStringTest")
    let cache = ""
    let tagStr = ""

    document.querySelectorAll(".item-tag").forEach(e => e.remove())
    cache = ""

    tags_list.forEach(e => {
        cache = `<div class="item-tag">
                    <span>${e.text}</span>
                    <button type="button" data-id="${e.id}" class="btn-rm-tag">
                        <i class="fas fa-times" onclick="HandleRmTags()"></i>
                    </button>
                 </div>`;
        wrapper_tags.insertAdjacentHTML('afterbegin', cache)
        tagStr = tagStr + e.text + ",";

        tagString.setAttribute('value', tagStr)
    })
    tags_length.textContent = `${tags_list.length} Tags`
    if (tags_list.length < 1 ){
        clearInput()
    }
}

function clearInput() {
    document.getElementById("tagStringTest").setAttribute('value', '');
}

function HandleRmTags(){
    const btns = document.querySelectorAll(".btn-rm-tag");
    if (btns.length > 0) {
        btns.forEach((e) => {
            e.onclick = function () {
                const data_id = Number(e.getAttribute("data-id"));
                tags_list = tags_list.filter(x => x.id != data_id);
                RenderTags();
            };
        });
    }
}