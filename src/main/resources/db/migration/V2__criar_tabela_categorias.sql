CREATE TABLE categorias (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    icone           VARCHAR(50),
    tipo_categoria  VARCHAR(20) NOT NULL,
    usuario_id      BIGINT REFERENCES usuarios(id)
);