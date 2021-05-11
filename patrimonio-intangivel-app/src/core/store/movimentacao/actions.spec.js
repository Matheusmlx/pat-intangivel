import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, state, mockResponseData, returnedResponse

const movimentacao = {
    id: 1,
    tipo: 'TRANSFERENCIA_DEFINITIVA',
    idPatrimonio: 1,
    orgaoDestino: 5,
    orgaoOrigem: 1,
    situacao: 'AGUARDANDO_RECEBIMENTO'
}

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    },
    get(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve()
        })
    },
    patch(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
    const context = jest.fn()

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true

        state = {
            movimentacao: {}
        }
    })

    it('ATUALIZAR_MOVIMENTACAO', async () => {
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO]({commit}, movimentacao)

        expect(url).toBe(`api/movimentacao/${movimentacao.id}`, movimentacao)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO, true)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_MOVIMENTACAO_POR_PATRIMONIO', async () => {
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO](context, movimentacao)

        expect(url).toBe(`api/movimentacoes-patrimonio/?idPatrimonio=${movimentacao.idPatrimonio}`)
        expect(returnedResponse).toBeTruthy()
    })

    it('CADASTRAR_MOVIMENTACAO', async () => {
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.CADASTRAR_MOVIMENTACAO]({commit}, movimentacao)

        expect(url).toBe('api/movimentacao', movimentacao)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO, true)
    })

    it('DELETAR_MOVIMENTACAO', async () => {

        returnedResponse = await actions[actionTypes.MOVIMENTACAO.DELETAR_MOVIMENTACAO](context, movimentacao.id)
        expect(url).toBe(`api/movimentacao/${movimentacao.id}`)
    })

    it('Deve fazer uma requisição que envie uma movimentação', async () => {
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.ENVIAR_MOVIMENTACAO](context, movimentacao)
        expect(url).toBe(`api/movimentacao/${movimentacao.id}/enviar`, movimentacao)
    })

    it('BUSCAR_MOVIMENTACAO_POR_ID', async () => {

        returnedResponse = await actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_ID](context, movimentacao.id)
        expect(url).toBe(`api/movimentacao/${movimentacao.id}`)
    })

    it('BUSCAR_PATRIMONIO_POR_MOVIMENTACAO', async () => {

        returnedResponse = await actions[actionTypes.MOVIMENTACAO.BUSCAR_PATRIMONIO_POR_MOVIMENTACAO](context, movimentacao.id)
        expect(url).toBe(`api/movimentacao/patrimonio/${movimentacao.id}`)
    })

    it('BUSCAR_TODAS_MOVIMENTACOES', async () => {
        state = {
            todasMovimentacoes: {
                filtros: {
                    conteudo: {
                        value: null,
                        default: null
                    },
                },
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [true]
                },
            },
        }

        returnedResponse = await actions[
            actionTypes.MOVIMENTACAO.BUSCAR_TODAS_MOVIMENTACOES
            ]({state})
        expect(url).toBe('api/movimentacoes?page=1&size=10')
        expect(returnedResponse).toBeTruthy()
    })

    it('BUSCAR_MOVIMENTACOES_PENDENTES', async () => {
        state = {
            todasMovimentacoesPendentes: {
                filtros: {
                    conteudo: {
                        value: null
                    },
                },
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [true]
                }
            },
        }

        let filtros ={
            conteudo: {
                value: null,
            default: null,
                    label: 'Pesquisa'
            },
            conteudoExtra: {
                value: 'AGUARDANDO_RECEBIMENTO',
            default: null,
                    label: 'Pesquisa'
            }
        }

        returnedResponse = await actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACOES_PENDENTES]({commit,state},filtros)
        expect(url).toBe('api/movimentacoes?conteudoExtra=AGUARDANDO_RECEBIMENTO&page=1&size=10')
        expect(returnedResponse).toBeTruthy()
    })

    it('RECEBER MOVIMENTAÇÃO', async () => {
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.RECEBER_MOVIMENTACAO](context, movimentacao.id)
        expect(url).toBe(`api/movimentacao/${movimentacao.id}/receber`)

    })

    it('REJEITAR MOVIMENTAÇÃO', async () => {
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.REJEITAR_MOVIMENTACAO](context, movimentacao.id)
        expect(url).toBe(`api/movimentacao/${movimentacao.id}/rejeitar`)

    })

    it('DEVE CADASTRAR DOCUMENTO', async () => {
        const documento = {
            idMovimentacao: 1,
            numero:"0005",
            tipo:"RECIBO"
        }
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO](context, documento)
        expect(url).toBe(`api/movimentacao/${documento.idMovimentacao}/documentos`,documento)
    })

    it('ATUALIZAR_DOCUMENTO_MOVIMENTACAO', async () => {
        const documento = {
            id: 1,
            idMovimentacao : 1
        }

        returnedResponse = await actions[actionTypes.MOVIMENTACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO]({commit}, documento)

        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/movimentacao/${documento.idMovimentacao}/documentos/${documento.id}`, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('DOCUMENTO_UPLOAD', async () => {
        const idMovimentacao = 5
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.DOCUMENTO.DOCUMENTO_UPLOAD_MOVIMENTACAO](context, idMovimentacao)
        expect(url).toBe('api/v1/arquivos')
    })

    it('DELETAR_DOCUMENTO', async () =>{
        const documento = { id:2,idMovimentacao:3,idTipoDocumento:2}
        state = {
            documentos:[],
            documentoBackup:{}
        }
        returnedResponse = await actions[actionTypes.MOVIMENTACAO.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO]({state}, documento)
        expect(url).toEqual('api/movimentacao/3/documentos/2')
    })
})
