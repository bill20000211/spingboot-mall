const apiUrl = 'http://localhost:8080'; // 後端 API URL

// 獲取所有產品
function fetchProducts() {
    fetch(`${apiUrl}/products`)
        .then(response => response.json())
        .then(data => {
            const productList = document.getElementById('product-list');
            productList.innerHTML = data.map(product => `
                <div>
                    <h3>${product.name}</h3>
                    <p>Category: ${product.category}</p>
                    <a href="product.html?id=${product.id}">View Details</a>
                </div>
            `).join('');
        });
}

// 獲取單個產品
function fetchProduct(productId) {
    fetch(`${apiUrl}/products/${productId}`)
        .then(response => response.json())
        .then(product => {
            const productDetail = document.getElementById('product-detail');
            productDetail.innerHTML = `
                <h2>${product.name}</h2>
                <p>Category: ${product.category}</p>
                <p>Description: ${product.description}</p>
            `;
        });
}

// 創建產品
function createProduct() {
    const productData = {
        name: document.getElementById('name').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value
    };

    fetch(`${apiUrl}/products`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
    }).then(response => {
        if (response.status === 201) {
            alert('Product created successfully');
            window.location.href = 'index.html';
        }
    });
}

// 獲取要更新的產品資料
function fetchProductToUpdate(productId) {
    fetch(`${apiUrl}/products/${productId}`)
        .then(response => response.json())
        .then(product => {
            document.getElementById('name').value = product.name;
            document.getElementById('category').value = product.category;
            document.getElementById('description').value = product.description;
        });
}

// 更新產品
function updateProduct(productId) {
    const productData = {
        name: document.getElementById('name').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value
    };

    fetch(`${apiUrl}/products/${productId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
    }).then(response => {
        if (response.status === 200) {
            alert('Product updated successfully');
            window.location.href = 'index.html';
        }
    });
}

// 刪除產品
function deleteProduct(productId) {
    fetch(`${apiUrl}/products/${productId}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.status === 204) {
            alert('Product deleted successfully');
            window.location.href = 'index.html';
        }
    });
}
