$(function() {
  let page = 0;
  const size = 30;

  function renderProducts(products) {
    const $list = $('#product-list');
    products.forEach(p => {
      const $col = $(
        `<div class="col-lg-3 col-md-4 mb-4">
           <div class="card h-100 shadow-sm">
             <img src="/images/product-${p.id}.jpg" class="card-img-top" alt="${p.name}">
             <div class="card-body">
               <h5 class="card-title text-truncate" title="${p.name}">${p.name}</h5>
               <p class="card-text mb-2 text-danger fw-bold">${p.price.toLocaleString()}원</p>
               <p class="card-text text-muted">재고: ${p.stock}개</p>
               <a href="/products/${p.id}" class="btn btn-sm btn-primary mt-2">상세보기</a>
             </div>
           </div>
         </div>`);
      $list.append($col);
    });
  }

  function loadProducts() {
    $.ajax({
      url: '/api/public/products',
      method: 'GET',
      data: { page, size },
      success: res => {
        renderProducts(res.content);
        if ((page + 1) * size >= res.totalElements) {
          $('#load-more').hide();
        } else {
          $('#load-more').show();
        }
      },
      error: err => console.error('상품 목록 로드 실패:', err)
    });
  }

  // 초기 로드
  loadProducts();

  // 더 보기 버튼
  $('#load-more').on('click', function() {
    page++;
    loadProducts();
  });
});