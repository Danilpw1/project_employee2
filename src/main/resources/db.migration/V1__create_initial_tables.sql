-- Таблица сотрудников
CREATE TABLE IF NOT EXISTS employees (
                                         id BIGSERIAL PRIMARY KEY,
                                         first_name VARCHAR(50) NOT NULL,
                                         last_name VARCHAR(50) NOT NULL,
                                         position VARCHAR(100) NOT NULL,
                                         salary NUMERIC(10,2) NOT NULL CHECK (salary > 0),
                                         department VARCHAR(100) NOT NULL,
                                         hire_date DATE NOT NULL,
                                         email VARCHAR(100) UNIQUE NOT NULL,
                                         phone VARCHAR(20) NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица клиентов
CREATE TABLE IF NOT EXISTS clients (
                                       id BIGSERIAL PRIMARY KEY,
                                       first_name VARCHAR(50) NOT NULL,
                                       last_name VARCHAR(50) NOT NULL,
                                       email VARCHAR(100) UNIQUE NOT NULL,
                                       phone VARCHAR(20) NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      created_at TIMESTAMP NOT NULL,
                                      status VARCHAR(20) NOT NULL,
                                      total_amount NUMERIC(10,2) DEFAULT 0,
                                      total_items INTEGER DEFAULT 0,
                                      client_id BIGINT NOT NULL,
                                      CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE SET NULL
);