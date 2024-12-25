/* <div>
            <div class="basket_img">
              <a><img src="images/basket/00155055500-p.jpg" /></a>
            </div>
            <div class="basket_product_info">
              <div class="basket_product_info_header">
                <div><a class="product_link">스웨이드 레더 점퍼</a></div>
                <div>⨉</div>
              </div>
              <div class="basket_pruduct_price">27,000₩</div>
              <div class="basket_pruduct_size_category">
                <span class="basket_product_size">XL (KR 105-110) </span>|<span
                  class="basket_product_category"
                  >샌드</span
                >
              </div>
              <div class="basket_product_count_container">
                <button class="minus_count">+</button>
                <div class="basket_product_count">
                  <span class="pruduct_count_num">1</span>
                </div>
                <button class="plus_count">-</button>
              </div>
            </div>
          </div>
 */
//장바구니 개수
let basketCount = 0;

//장바구니 목록을 가져오는 함수
async function getBasketList() {
  try {
    const response = await fetch("json/basketList.json", {
      method: "get",
      headers: { contentType: "application/json" },
    });
    const basketList = await response.json();

    //각 json객체를 순환

    let html = "";
    let PK_BASKET_NO = "";
    let FK_MEMBER_NO = "";
    let FK_PRODUCT_DETAIL_NO = "";
    let BASKET_QUANTITY = "";
    let PRODUCT_NO = "";
    let PRODUCT_SIZE = "";
    let PRODUCT_INVENTORY = "";
    let PRODUCT_NAME = "";
    let PRODUCT_PRICE = "";
    let COLOR_NAME = "";
    let PRODUCT_IMAGE_PATH = "";
    let PRODUCT_IMAGE_NAME = "";
    basketCount = 0;
    basketList.forEach((basket) => {
      //   console.log(basket);
      //   json 객체의 값을 추출하기 위한 for문
      for (key in basket) {
        // console.log(basket[key]);
        switch (key) {
          case "PK_BASKET_NO":
            PK_BASKET_NO = basket[key];
            break;
          case "FK_MEMBER_NO":
            FK_MEMBER_NO = basket[key];
            break;
          case "FK_PRODUCT_DETAIL_NO":
            FK_PRODUCT_DETAIL_NO = basket[key];
            break;
          case "BASKET_QUANTITY":
            BASKET_QUANTITY = basket[key];
            break;
          case "PRODUCT_NO":
            PRODUCT_NO = basket[key];
            break;
          case "PRODUCT_SIZE":
            PRODUCT_SIZE = basket[key];
            break;
          case "PRODUCT_INVENTORY":
            PRODUCT_INVENTORY = basket[key];
            break;
          case "PRODUCT_NAME":
            PRODUCT_NAME = basket[key];
            break;
          case "PRODUCT_PRICE":
            PRODUCT_PRICE = basket[key];
            break;
          case "COLOR_NAME":
            COLOR_NAME = basket[key];
            break;
          case "PRODUCT_IMAGE_PATH":
            PRODUCT_IMAGE_PATH = basket[key];
            break;
          case "PRODUCT_IMAGE_NAME":
            PRODUCT_IMAGE_NAME = basket[key];
            break;
        }
      } // end of for-------------------------------
      html += `<div id="basket_${PK_BASKET_NO}" class="basket_item" data-basket_no="${PK_BASKET_NO}" >
            <div class="basket_img">
              <a><img src="${PRODUCT_IMAGE_PATH}" /></a>
            </div>
            <div class="basket_product_info">
              <div class="basket_product_info_header">
                <div><a class="product_link">${PRODUCT_NAME}</a></div>
                <div id= "basket_delete_${PK_BASKET_NO}" class = "basket_delete">⨉</div>
              </div>
              <div class="basket_pruduct_price"><span class="price_text" data-price ="${PRODUCT_PRICE}">${
        PRODUCT_PRICE * BASKET_QUANTITY
      }</span>₩</div>
              <div class="basket_pruduct_size_category">
                <span class="basket_product_size">${PRODUCT_SIZE}</span>&nbsp;|&nbsp;<span
                  class="basket_product_category"
                  >${COLOR_NAME}</span
                >
              </div>
              <div class="basket_product_count_container">
                <button class="plus_count">+</button>
                <div class="basket_product_count">
                  <span class="pruduct_count_num">${BASKET_QUANTITY}</span>
                </div>
                <button class="minus_count">-</button>
              </div>
            </div>
          </div>`;
      //장바구니의 총 개수를 증가
      basketCount = basketCount + 1;
    }); // end of forEach-------------------------------
    return html;
  } catch {
    console.log("error");
  }
}

async function deleteBasketList(basket_no) {
  try {
    const response = await fetch("json/delete.txt", {
      method: "get",
      headers: { "Content-Type": "text/plain" },
      body: basket_no,
    });
    response.text().then((text) => {
      if (text == "success") {
        console.log("성공");
      }
    });
  } catch {
    console.log("error");
  }
}

//장바구니의 총 가격을 계산하는 함수
function calculateTotalPrice() {
  //각 장바구니의 가격정보 불러오기
  const priceList = document.querySelectorAll("span.price_text");
  // 총 가격 생성성
  let totlaPrice = 0;
  priceList.forEach((element) => {
    totlaPrice += Number(element.textContent);
  });
  document.querySelector("span#total_price").textContent = totlaPrice;
}

document.addEventListener("DOMContentLoaded", function () {
  const basket_list = document.querySelector("div#basket_list");

  //장바구니 목록을 가져오기—
  getBasketList().then((html) => {
    basket_list.innerHTML = html;
    //장바구니 목록을 가져온 후 총 가격 계산
    calculateTotalPrice();
    document.querySelector(
      "span#basket_count"
    ).textContent = `(${basketCount})`;
  }); // end of getBasketList().then((html) => {

  //장바구니 목록의 요소를 클릭했을 경우
  basket_list.addEventListener(
    "click",
    (e) => {
      const basket_item = e.target.closest("div.basket_item");
      const basket_No = basket_item.getAttribute("data-basket_no");
      //상품의 가격을 가져온다
      const product_price = basket_item.querySelector("span.price_text");
      //삭제버튼 클릭시시
      if (e.target.className == "basket_delete") {
        // 요소를 삭제시킨다
        basket_item.remove();
        //삭제시킨 상품의 가격을 뺀다
        calculateTotalPrice();
      }
      //+ 버튼 클릭시시
      else if (e.target.className == "plus_count") {
        // console.log(e.target.nextElementSibling.firstElementChild);
        //span 태그를 가져온다
        const count_num = e.target.nextElementSibling.firstElementChild;
        //상품의 가격에서 증가시킨다다
        const after_price =
          Number(product_price.textContent) +
          Number(product_price.getAttribute("data-price"));
        //span태그의 값을 증가시킨다
        count_num.textContent = Number(count_num.textContent) + 1;
        product_price.textContent = after_price;
        // 가격을 다시 계산한다.
        calculateTotalPrice();
      }
      // -버튼 클릭시
      else if (e.target.className == "minus_count") {
        // console.log(e.target.previousElementSibling.firstElementChild);
        //span 태그를 가져온다
        const count_num = e.target.previousElementSibling.firstElementChild;
        //상품의 가격에서 감소시킨다
        console.log(product_price.getAttribute("data_price"));
        const after_price =
          Number(product_price.textContent) -
          Number(product_price.getAttribute("data-price"));

        if (count_num.textContent == 1) {
          console.log(basket_item);
          return;
        }
        //span태그의 값을 감소시킨다
        count_num.textContent = Number(count_num.textContent) - 1;
        product_price.textContent = after_price;
        // 가격을 다시 계산한다.
        calculateTotalPrice();
      }
    },
    true
  );
});
