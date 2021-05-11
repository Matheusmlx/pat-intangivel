import {actionTypes, mutationTypes} from '@/core/constants'
import actions from './actions'

let url, body, mockResponseData, returnedResponse

const patrimonio = {
    id: 2,
    nome: 'Adobe',
    descricao: 'Teste',
    situaco: 'EM_ELABORACAO',
    reconhecimento: null,
    dataAquisicao: null,
    dataNL: null,
    numeroNL: null,
    fornecedor: null,
    dataVencimento: null,
    tipo: 'SOFTWARES',
    estado: 'PRONTO_PARA_USO',
    orgao: 3,
    setor: 4,
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
    },
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
            loki: {},
            documentoBackup: {},
            documentos: []
        }
    })

    it('CADASTRAR_PATRIMONIO', async () => {
        const packageJson = {tipo: 'SOFTWARE'}
        returnedResponse = await actions[actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]({commit}, packageJson)

        expect(url).toBe('api/patrimonios')

        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.SET_PATRIMONIO, true)
    })
    it('BUSCAR_TODOS_PATRIMONIOS', async () => {
        state = {
            todosPatrimonios: {
                filtros: {
                    tipoPatrimonio: {
                        value: null,
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
            actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS
            ]({state})
        expect(url).toBe('api/patrimonios?page=1&size=10')
        expect(returnedResponse).toBeTruthy()
    })
    it('BUSCAR_PATRIMONIO', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]({commit}, idPatrimonio)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.SET_PATRIMONIO, true)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}`)
        expect(returnedResponse).toBeTruthy()
    })
    it('ATUALIZAR_PATRIMONIO', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]({commit}, patrimonio)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/patrimonios/${patrimonio.id}`, patrimonio)

        setTimeout(function () {
            expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        }, 300)

    })
    it('DELETAR_PATRIMONIO', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.DELETAR_PATRIMONIO](context, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}`)
    })
    it('Deve cadastrar um novo Documento', async () => {
        const documento = {idPatrimonio: 1}
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO](context, documento)
        expect(url).toBe(`api/patrimonios/${documento.idPatrimonio}/documentos`, documento)
    })
    it('Deve buscar os documentos relacionados ao id do Patrimonio', async () => {
        const idPatrimonio = 2
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]({commit}, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}/documentos`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.SET_DOCUMENTOS, returnedResponse)

    })
    it('Deve atualizar os Documentos ', async () => {
        const documento = {idDocumento: undefined, idPatrimonio: 3}
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO]({commit}, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/patrimonios/${documento.idPatrimonio}/documentos/${documento.idDocumento}`, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })
    it('Deve deletar patrimônio não alterado', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.DELETAR_PATRIMONIO_NAO_ALTERADO](context, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}/nao-alterado`)
    })
    it('Deve reverter a ativação', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.REVERTER_ATIVACAO](context, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}/desativar`)
    })
    it('Deve ativar o patrimônio', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.ATIVA_PATRIMONIO](context, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}`)
    })
    it('Deve buscar patrimônios para visualização', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID]({commit}, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}/visualizacao-completa`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.SET_PATRIMONIO, true)
    })
    it('Deve validar número único de documento', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO](context, idPatrimonio)
        expect(url).toBe(`api/patrimonios/${idPatrimonio}/documentos`)
    })
    it('Deve buscar tipos de documento', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO](context, idPatrimonio)
        expect(url).toBe('api/tiposdocumentos?page=1&size=12')
    })
    it('Deve realizar upload de documento', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD](context, idPatrimonio)
        expect(url).toBe('api/v1/arquivos')
    })
    it('Deve realizar download de documento', async () => {
        const anexo = 5
        window.URL.createObjectURL = jest.fn()

        await actions[actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_DOWNLOAD](context, anexo)
        expect(url).toBe('api/v1/arquivos')
    })
    it('Deve deletar documento', async () => {
        const documento = {
            id: 1,
            idPatrimonio: 5
        }
        returnedResponse = await actions[actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO]({state}, documento)
        expect(url).toBe(`api/patrimonios/${documento.idPatrimonio}/documentos/${documento.id}`)
    })
    it('Deve buscar amortizações', async () => {
        const idPatrimonio = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_AMORTIZACOES](context, idPatrimonio)
        expect(url).toBe(`api/amortizacoes/${idPatrimonio}`)
    })
})
