CREATE TABLE conta (
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    saldo        NUMERIC(19, 2) NOT NULL DEFAULT 0,
    tipo_conta   VARCHAR(20) NOT NULL,
    data_criacao TIMESTAMP,
    usuario_id   BIGINT NOT NULL REFERENCES usuarios(id)
);