import {shallowMount} from '@vue/test-utils'
import PatrimonioDetalheDocumentos from './PatrimonioDetalheDocumentos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import { actionTypes, mutationTypes } from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'

describe('PatrimonioDetalheDocumentos', () => {
    let wrapper, localVue, store, state, mutations, $validator, errors, actions, router,vuetify, directives, $t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const defaultMount = (stubs) => shallowMount(PatrimonioDetalheDocumentos, {
        localVue,
        directives,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                CamposPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
    })

    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
        }
    }

    const documentos = [{id:0, numero: '141', data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'notafiscal.pdf' },
        {id:1, numero: '142', data: '', valor: '', idTipoDocumento: 2, uriAnexo: 'notafiscal.pdf' },
        {id:2, numero: '143', data: '', valor: '', idTipoDocumento: 2, uriAnexo: 'notafiscal.pdf' },
        {id:3, numero: '144', data: '', valor: '', idTipoDocumento: 3, uriAnexo: 'notafiscal.pdf' }]

    const patrimonio = {
        id: 48,
        nome: 'teste',
        descricao: null,
        situacao: null,
        reconhecimento: null,
        dataAquisicao: null,
        dataNL: null,
        numeroNL: null,
        fornecedor: null,
        dataVencimento: null,
        tipo: 'DIREITOS_AUTORAIS',
        estado: null,
        orgao: 3,
        setor: 4,
    }

    const tipoDocumentos = {
        items:[
            {
                id:1,descricao:'Nota Fiscal'
            },
            {
                id:2,descricao:'Recibo'
            },
            {
                id:3,descricao:'Contrato'
            },
            {
                id:4,descricao:'Nota de Lançamento'
            }
        ]
    }

    const anexoRetorno = {uriAnexo: 'uriAnexo'}

    const dado = { numero: '141', data: ' ', idPatrimonio:48, valor: '698', idTipoDocumento: 2, uriAnexo: 'notafiscal.pdf' }

    const dadoComId = {id:4, numero: '142', data: ' ', idPatrimonio:48, valor: '698', idTipoDocumento: 2, uriAnexo: 'notafiscal.pdf' }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn()
        }
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        }
                    ]
                }
            },
            documentos: [{}]
        }
        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }
        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'MenuDetalhe',
                    params: {
                        id: patrimonio.id,
                    },
                },
            },
        }

        actions = {
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos),
            [actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos.items),
            [actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO]:jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD]:jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]: jest.fn().mockReturnValue([]),
        }
        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            ['SET_UPLOADED_FILES']: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_DOCUMENTOS]: jest.fn()
        }

        store = new Vuex.Store({ mutations, state, actions })
    })

    describe('Mounted', () => {
        it('Deve montar PatrimonioDetalheDocumentos',async  () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.id)

        })
    })

    describe('Methods', () => {

        it('Deve salvar o tipo do documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvar(dadoComId)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO].mock.calls[0][1]).toEqual(dadoComId)
        })

        it('Deve adicionar um novo Documento', async() => {
            wrapper = defaultMount()
            wrapper.vm.docNovo= {}
            wrapper.vm.novoDocumento()
            await flushPromises()

            expect(wrapper.vm.adicionando).toEqual(true)
            expect(wrapper.vm.docNovo.idPatrimonio).toEqual(wrapper.vm.$route.params.id)
        })

        it('Deve cadastrar um Documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvar(dado)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO]).toBeDefined()
        })

        it('Deve atualizar um Documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvar(dadoComId)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO]).toBeDefined()
        })

        it('Deve deletar um documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoDeletarDocumento(dadoComId, 1)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO]).toHaveBeenCalled()
        })

        it('Deve deletar um documento novo', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoDeletarDocumentoNovo()
            await flushPromises()

            expect(wrapper.vm.adicionando).toEqual(false)
            expect(wrapper.vm.docNovo).toEqual({})
        })

        it('Deve salvar o anexo', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvarAnexo(anexoRetorno)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD]).toHaveBeenCalled()
        })

        it('Deve salvar o nome do componente que está editando', () => {
            wrapper = defaultMount()
            wrapper.vm.setaEditando('Numero')

            expect(wrapper.vm.estaEditando).toEqual(['Numero'])
        })

        it('Deve retirar o nome do componente que está editando', () => {
            wrapper = defaultMount()
            wrapper.vm.estaEditando = ['Numero']
            wrapper.vm.retiraEditando('Numero')

            expect(wrapper.vm.estaEditando).toEqual([])
        })

        it('Deve Finalizar a edição do documento antes de criar um novo', async() => {
            wrapper = defaultMount()
            wrapper.vm.setaEditando('Numero')
            wrapper.vm.novoDocumento()
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize a edição do documento antes de criar um novo')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve Finalizar o cadastro para realizar a edição', async() => {
            wrapper = defaultMount()
            wrapper.vm.estaAdicionando()
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para realizar a edição')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Não deve existir documento com este número', async () => {
            wrapper = defaultMount()
            wrapper.vm.validarNumeroUnico(dado)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO].mock.calls[0][1]).toEqual(48)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
        })

        it('Deve existir documento com este número', async () => {
            actions = {
                [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos),
                [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO] : jest.fn().mockReturnValue(tipoDocumentos),
                [actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]: jest.fn().mockReturnValue(documentos),
            }
            wrapper = defaultMount()

            wrapper.vm.validarNumeroUnico(dadoComId)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO].mock.calls[0][1]).toEqual(48)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Já existe um documento com esse número')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
        })

    })

})

