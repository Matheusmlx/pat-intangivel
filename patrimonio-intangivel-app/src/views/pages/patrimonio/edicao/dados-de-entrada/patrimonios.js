export const patrimonio = {
    id: 48,
    nome: 'teste',
    orgao: 3,
    setor: 4,
    descricao: 'teste',
    situacao: 'teste',
    reconhecimento: 'teste',
    dataAquisicao: 'teste',
    valorAquisicao: '10000',
    dataNL: '2020-05-05 03:00:00.000000',
    numeroNL: '12314NL12365',
    fornecedor: {
        id: 0,
        cnpj: '46456456',
        nome: 'cnpj 0',
    },
    dataVencimento: '2020-12-12 03:00:00.000000',
    dataAtivacao: '2020-02-10 03:00:00.000000',
    tipo: 'DIREITOS_AUTORAIS',
    estado: 'ATIVO',
    contaContabil: 1
}

export const patrimonioNL = {
    id: 48,
    nome: 'teste',
    descricao: 'descricao',
    situacao: 'EM_ELABORACAO',
    reconhecimento: 'AQUISICAO_SEPARADA',
    dataAquisicao: null,
    valorAquisicao: 300,
    dataNL: '05/10/2020',
    numeroNL: '8888NL9998',
    fornecedor: null,
    dataVencimento: '2020-12-13 03:00:00.000000',
    dataAtivacao: '2020-03-24 03:00:00.000000',
    tipo: 'DIREITOS_AUTORAIS',
    estado: 'PRONTO_PARA_USO',
    orgao: {
        id: 0,
        sigla: 'AGETEC',
        nome: 'Agência Municipal de Tecnologia da Informação e Inovação',
    },
    setor: {
        id: 0,
        sigla: 'DAS',
        nome: 'Diretoria de Atendimento e Suporte'
    },
}

export const patrimonioCompleto = {
    id: 48,
    nome: 'teste',
    descricao: 'descricao',
    situacao: 'EM_ELABORACAO',
    reconhecimento: 'AQUISICAO_SEPARADA',
    dataAquisicao: '2020-02-13 03:00:00.000000',
    valorAquisicao: '10000',
    dataNL: '2020-08-15 03:00:00.000000',
    numeroNL: '12314NL12365',
    fornecedor: 1,
    dataVencimento: '2020-12-25 03:00:00.000000',
    dataAtivacao: '2020-02-26 03:00:00.000000',
    tipo: 'DIREITOS_AUTORAIS',
    estado: 'EM_DESENVOLVIMENTO',
    orgao: {
        id: 0,
        sigla: 'AGETEC',
        nome: 'Agência Municipal de Tecnologia da Informação e Inovação',
    },
    setor: {
        id: 0,
        sigla: 'DAS',
        nome: 'Diretoria de Atendimento e Suporte'
    },
    contaContabil: 1
}

export const patrimonioIncompleto = {
    id: 48,
    nome: 'teste',
    descricao: '',
    situacao: '',
    reconhecimento: null,
    dataAquisicao: null,
    valorAquisicao: 300,
    dataNL: null,
    numeroNL: '',
    fornecedor: null,
    dataAtivacao: '2020-01-31 03:00:00.000000',
    dataVencimento: '2020-02-13 03:00:00.000000',
    tipo: '',
    estado: ''
}
