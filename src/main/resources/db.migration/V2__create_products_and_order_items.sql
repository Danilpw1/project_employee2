-- Таблица товаров
CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(200) UNIQUE NOT NULL,
                                        description TEXT,
                                        price NUMERIC(10,2) NOT NULL CHECK (price > 0),
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Промежуточная таблица для связи заказов и товаров
CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           quantity INTEGER NOT NULL CHECK (quantity > 0),
                                           unit_price NUMERIC(10,2) NOT NULL CHECK (unit_price > 0),
                                           total_price NUMERIC(10,2) NOT NULL CHECK (total_price > 0),
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                                           CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
                                           CONSTRAINT unique_order_product UNIQUE (order_id, product_id)
);