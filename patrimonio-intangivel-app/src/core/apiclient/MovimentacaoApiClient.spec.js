import movimentacaoApiClient from './MovimentacaoApiClient'

let url, config, mockRetornoApi, verboHttp

let dados = {
    id: 1
}

const movimentacao = {
    id: 1,
    tipo: 'TRANSFERENCIA_DEFINITIVA',
    idPatrimonio: 1,
    orgaoDestino: 5,
    orgaoOrigem: 1,
    situacao: 'AGUARDANDO_RECEBIMENTO'
}

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    put(_url) {
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            verboHttp = 'delete'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    patch(_url) {
        return new Promise(resolve => {
            verboHttp = 'patch'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('MovimentacaoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('atualizar', async () => {
        const {data} = await movimentacaoApiClient.atualizar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacao/${dados.id}`, dados)
    })

    it('buscarMovimentacaoPorPatrimonio', async () => {
        const {data} = await movimentacaoApiClient.buscarMovimentacaoPorPatrimonio(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes-patrimonio/?idPatrimonio=${movimentacao.idPatrimonio}`)
    })

    it('cadastrar', async () => {
        const {data} = await movimentacaoApiClient.cadastrar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/movimentacao', dados)

    })

    it('deletar', async () => {
        const {data} = await movimentacaoApiClient.deletar(movimentacao.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/movimentacao/${movimentacao.id}`)
    })

    it('enviar movimentacao', async () => {
        const {data} = await movimentacaoApiClient.enviarMovimentacao(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toBe(`api/movimentacao/${movimentacao.id}/enviar`)
    })

    it('buscar por id', async () => {
        const {data} = await movimentacaoApiClient.buscarPorId(movimentacao.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacao/${movimentacao.id}`)
    })

    it('buscar patrimonio por movimentacao id', async () => {
        const {data} = await movimentacaoApiClient.buscarPatrimonioPorMovimentacao(movimentacao.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacao/patrimonio/${movimentacao.id}`)
    })

    it('buscar todos', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc: []
        }
        const filtros = {
            conteudo: {
                value: null,
                default: null
            }
        }
        const {data} = await movimentacaoApiClient.buscarTodos(filtros, paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/movimentacoes?page=1&size=100')
    })

    it('buscar pendentes', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc: []
        }
        const filtros = {
            conteudo: {
                value: null
            },
            conteudoExtra: {
                value: 'AGUARDANDO_RECEBIMENTO'
            }
        }
        const {data} = await movimentacaoApiClient.buscarPendentes(filtros, paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/movimentacoes?conteudoExtra=AGUARDANDO_RECEBIMENTO&page=1&size=100')
    })

    it('deve receber uma movimentação', async () => {
        const {data} = await movimentacaoApiClient.receberMovimentacao(movimentacao.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacao/${movimentacao.id}/receber`)
    })

    it('deve rejeitar uma movimentação', async () => {
        const {data} = await movimentacaoApiClient.rejeitarMovimentacao(movimentacao.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacao/${movimentacao.id}/rejeitar`)
    })

    it('deve cadastrar um documento', async () => {
        const documento = {
            idMovimentacao: 1,
            numero: "0005",
            tipo: "RECIBO"
        }
        const {data} = await movimentacaoApiClient.cadastrarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toBe(`api/movimentacao/${documento.idMovimentacao}/documentos`, documento)
    })

    it('deve atualizar o documento da movimentação', async () => {
        const documento = {
            id: 1,
            idMovimentacao: 1
        }
        const {data} = await movimentacaoApiClient.atualizarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacao/${documento.idMovimentacao}/documentos/${documento.id}`, documento)
    })

    it('uploadAnexoDocumento', async () => {
        const formData = {}
        const {data} = await movimentacaoApiClient.uploadAnexoDocumento(formData)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/v1/arquivos')
    })

    it('deve Deletar um documento', async () => {
        const documento = {id: 1, idMovimentacao: 2}
        const {data} = await movimentacaoApiClient.deletarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual('api/movimentacao/2/documentos/1')
    })
})
