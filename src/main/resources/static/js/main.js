$(document).ready(function() {
    // Load products on the main page
    if ($('#product-list').length) {
        $.ajax({
            url: '/api/products',
            type: 'GET',
            success: function(products) {
                let productList = $('#product-list');
                products.forEach(function(product) {
                    let productCard = `
                        <div class="col">
                            <div class="card shadow-sm">
                                <a href="/product/${product.id}">
                                    <img src="${product.thumbnailUrl}" class="card-img-top" alt="${product.name}">
                                </a>
                                <div class="card-body">
                                    <h5 class="card-title">${product.name}</h5>
                                    <p class="card-text price">${product.price}원</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="btn-group">
                                            <a href="/cart/add/${product.id}" class="btn btn-sm btn-outline-secondary">Add to Cart</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                    productList.append(productCard);
                });
            },
            error: function(err) {
                console.error('Error fetching products:', err);
            }
        });
    }

    // Handle login form submission
    $('#login-form').on('submit', function(e) {
        e.preventDefault();
        let username = $('#username').val();
        let password = $('#password').val();

        // TODO: Implement login logic using AJAX
        console.log('Login attempt with:', { username, password });
    });

    // Handle signup form submission
    $('#signup-form').on('submit', function(e) {
        e.preventDefault();
        let username = $('#username').val();
        let nickname = $('#nickname').val();
        let email = $('#email').val();
        let password = $('#password').val();

        // TODO: Implement signup logic using AJAX
        console.log('Signup attempt with:', { username, nickname, email, password });
    });
});
