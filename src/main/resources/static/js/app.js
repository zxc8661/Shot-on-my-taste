document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/public/products')
        .then(resp => resp.json())
        .then(data => {
            const list = document.getElementById('product-list');
            const products = data.content || [];
            products.forEach(p => {
                const col = document.createElement('div');
                col.className = 'col';
                const price = p.price ? new Intl.NumberFormat('ko-KR', {style: 'currency', currency: 'KRW'}).format(p.price) : '';
                col.innerHTML = `
                <div class="card h-100">
                    <img src="${p.img || ''}" class="card-img-top" onerror="this.src='https://via.placeholder.com/150';">
                    <div class="card-body">
                        <h5 class="card-title">${p.productName}</h5>
                        <p class="card-text">${price}</p>
                    </div>
                </div>`;
                list.appendChild(col);
            });
        })
        .catch(err => console.error('Failed to load products', err));
});
