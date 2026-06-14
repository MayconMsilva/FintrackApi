CREATE TABLE transacao (
    id                   BIGSERIAL PRIMARY KEY,
    descricao            VARCHAR(100) NOT NULL,
    valor                NUMERIC(19, 2) NOT NULL,
    tipo_transacao       VARCHAR(20) NOT NULL,
    data_transacao       TIMESTAMP,
    conta_id             BIGINT NOT NULL REFERENCES conta(id),
    categoria_id         BIGINT REFERENCES categorias(id),
    transacao_origem_id  BIGINT
);