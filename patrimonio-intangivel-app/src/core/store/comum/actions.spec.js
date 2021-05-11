import actions from './actions'
import { actionTypes, mutationTypes, produto } from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({ data: mockResponseData })
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
    const context = jest.fn()
    let state

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true
        state = {
            loki: {
                product: {
                    id: 401,
                    name: 'patrimonio-intangivel'
                }
            }
        }
    })

    it('BUSCAR_PRODUTO_POR_NOME', async () => {
        const packageJson = { name: 'patrimonio-intangivel' }
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_PRODUTO_POR_NOME]({ commit }, packageJson)
        expect(url).toBe('/hal/public/produtos?produtoNome=patrimonio-intangivel')
        expect(commit).toHaveBeenCalledWith(mutationTypes.COMUM.SET_PRODUTO, true)
    })

    it('BUSCAR_USUARIO_LOGADO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_USUARIO_LOGADO]({ commit, state })
        expect(url).toBe(`/hal/usuario/sessao?produtoId=${state.loki.product.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.COMUM.SET_USUARIO_LOGADO, true)
    })

    it('BUSCAR_LINK_EDITAR_USUARIO', async () => {
        const payload = { 'produto': 'patrimonio-intangivel', 'uri': 'http://localhost/' }

        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_LINK_EDITAR_USUARIO]({ state })
        expect(url).toBe('/hal/editarUsuario')
        expect(body).toEqual(payload)
        expect(returnedResponse).toBeTruthy()
    })
    it('BUSCAR_BUSCAR_LINK_SIGAPATRIMONIO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_LINK_SIGAPATRIMONIO]()
        expect(returnedResponse).toEqual(`${window.location.origin}/${produto.URL_SIGAPATRIMONIO}`)
    })
    it('ATUALIZAR_SESSAO_DOMINIO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.ATUALIZAR_SESSAO_DOMINIO]({ state })
        const produtoId = state.loki.product.id
        const dominioTipoCliente = 'ESTRUTURA_ORGANIZACIONAL'
        expect(url).toBe(`/hal/usuario/sessao/atualizarSessaoMultiplosDominios?produtoId=${produtoId}&dominioTipoCliente=${dominioTipoCliente}`)
    })
    it('BUSCAR_FORNECEDORES', async () => {
        const cnpj = '09087060120'
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FORNECEDORES](context,cnpj)
        expect(url).toBe(`api/fornecedores?conteudo=${cnpj}&page=1&size=100`)
    })
    it('BUSCAR_FORNECEDOR_POR_ID', async () => {
        const id = 5
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID](context,id)
        expect(url).toBe(`api/fornecedores/${id}`)
    })
    it('BUSCAR_SETORES', async () => {
        const orgaoId = 5
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_SETORES](context,orgaoId)
        expect(url).toBe(`api/unidades-organizacionais/${orgaoId}`)
    })
    it('BUSCAR_TODOS_ORGAOS', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]()
        expect(url).toBe('api/unidades-organizacionais?sort=sigla&direction=ASC')
    })
    it('BUSCAR_PARAMETROS', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_PARAMETROS]({commit})
        expect(commit).toHaveBeenCalledWith(mutationTypes.COMUM.SET_PARAMETROS,returnedResponse)
        expect(url).toBe('api/configuracao/parametros')
    })
    it('BUSCAR_FUSO_HORARIO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FUSO_HORARIO]()
        expect(url).toBe('api/fuso-horario')
    })
})
