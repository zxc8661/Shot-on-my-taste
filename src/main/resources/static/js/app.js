function fetchProducts() {
    $.get('/api/public/products', function(data) {
        var list = $('#product-list');
        list.empty();
        $.each(data.content, function(i, product) {
            var item = $('<div class="product-item">');
            item.append($('<h3>').text(product.productName));
            item.append($('<p>').text('Price: ' + product.price));
            if (product.img) {
                item.append($('<img>').attr('src', product.img).attr('alt', product.productName));
            }
            list.append(item);
        });
    });
}

$(document).ready(function() {
    fetchProducts();
});