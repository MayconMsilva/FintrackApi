-- Categorias padrão do sistema — disponíveis para todos os usuários
INSERT INTO categorias (nome, icone, tipo_categoria, usuario_id) VALUES
    ('Alimentação',   'utensils',       'SISTEMA', NULL),
    ('Transporte',    'car',            'SISTEMA', NULL),
    ('Saúde',         'heart-pulse',    'SISTEMA', NULL),
    ('Educação',      'book',           'SISTEMA', NULL),
    ('Lazer',         'gamepad',        'SISTEMA', NULL),
    ('Moradia',       'house',          'SISTEMA', NULL),
    ('Salário',       'wallet',         'SISTEMA', NULL),
    ('Investimentos', 'trending-up',    'SISTEMA', NULL),
    ('Outros',        'circle-ellipsis','SISTEMA', NULL);