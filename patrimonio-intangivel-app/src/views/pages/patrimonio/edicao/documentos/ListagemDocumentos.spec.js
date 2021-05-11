import {shallowMount} from '@vue/test-utils'
import ListagemDocumentos from './ListagemDocumentos'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('ListagemDocumentos', () => {
    let wrapper, localVue, store, state, mutations, router, actions, errors, $validator, $gtag

    const documentos = [{id:1,numero: 141, data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'uriAnexo'},
        {numero: 142, data: '', valor: '', idTipoDocumento: 2, uriAnexo: 'uriAnexo'},
        {numero: 143, data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'uriAnexo'},
        {numero: 144, data: '', valor: '', idTipoDocumento: 3, uriAnexo: 'uriAnexo'}]


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
        items: [
            {
                id: 1, descricao: 'Nota Fiscal'
            },
            {
                id: 2, descricao: 'Recibo'
            },
            {
                id: 3, descricao: 'Contrato'
            },
            {
                id: 4, descricao: 'Nota de Lançamento'
            }
        ],
    }

    const dado = {
        numero: 141,
        data: ' ',
        idPatrimonio: 48,
        valor: '0',
        idTipoDocumento: 1,
        uriAnexo: 'notafiscal.pdf'
    }

    const dadoComId = {
        id: 0,
        numero: 141,
        idPatrimonio: 48,
        idTipoDocumento: 'Nota Fiscal',
        uriAnexo: 'notafiscal.pdf'
    }

    const anexoRetorno = {uriAnexo: 'uriAnexo', numero: 1234, idTipoDocumento: 3}

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            documentos: {},
            items: [],
            loki: {
                user: {
                    domainId: 1,
                    domains: [],
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        },
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Retroativo'
                        },
                    ]
                }
            },
            documentoVazio: true,
            showDesfazer: false,
            rota: {
                origem: 'Inicial'
            },
            patrimonio: {
                documentos: [{}],
            }
        }

        errors = {
            collect: jest.fn(),
        }

        $gtag = {
            event: jest.fn()
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'DocumentosEdicao',
                    params: {
                        id: patrimonio.id,
                    },
                },
            },
        }

        actions = {
            [actionTypes.PATRIMONIO.ATIVA_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos),
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_DOWNLOAD]: jest.fn(),
            [actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD]: jest.fn().mockReturnValue(anexoRetorno),
            [actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]: jest.fn().mockReturnValue(documentos),
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_DOCUMENTOS]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.REMOVER_DOCUMENTOS]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({state, mutations, actions})


    })

    describe('Mounted', () => {
        it('Deve montar listagemDocumentos', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.id)

        })
    })

    describe('Methods', () => {
        it('Deve ativar o patrimonio', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.tratarEventoAtivar()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.ATIVA_PATRIMONIO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonio', params: {id: patrimonio.id}})
        })
        it('Deve buscar os Documentos', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.buscarDocumentos()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve baixar o anexo', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.baixarAnexo('')
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_DOWNLOAD]).toHaveBeenCalled()
        })

        it('Deve salvar o anexo', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            await flushPromises()
            wrapper.vm.tratarEventoSalvarAnexo(anexoRetorno, 0)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD]).toHaveBeenCalled()
        })

        it('Deve salvar um Documento', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.tratarEventoSalvar(dado)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve atualizar um Documento', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.tratarEventoSalvar(dadoComId)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve excluir um Documento sem id', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.remover(dado)
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.REMOVER_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })
        it('Deve excluir um Documento com id', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store: new Vuex.Store({state, mutations, actions}),
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.remover(dadoComId)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO].mock.calls[0][1]).toEqual(dadoComId)
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
            expect(wrapper.vm.showDesfazer).toEqual(true)
        })

        it('Deve voltar para dados de entrada edição', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.tratarEventoVoltar()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'DadosDeEntradaEdicao', params: {id: patrimonio.id}})
        })

        it('Deve Finalizar a edição do documento antes de criar um novo', () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                }
            })
            wrapper.vm.documentoVazio = false
            wrapper.vm.novoDocumento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para cadastrar novos documentos')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve Validar cadastro finalizado', () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                }
            })
            wrapper.vm.documentoVazio = false

            expect(wrapper.vm.validarCadastrosFinalizados()).toEqual(false)
        })

        it('Deve criar um novo documento', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.documentoVazio = true
            wrapper.vm.novoDocumento()

            expect(wrapper.vm.documentos[0].idPatrimonio).toEqual(48)
        })

        it('Não deve criar um novo documento', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.documentoVazio = false
            wrapper.vm.novoDocumento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para cadastrar novos documentos')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Não deve existir documento com este número', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            const index = 0
            const documentoValidar = {id:1,idTipoDocumento:1,numero:141}
            wrapper.vm.validarNumeroUnico(documentoValidar, index)

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO].mock.calls[0][1]).toEqual(48)
            expect(wrapper.vm.documentos[index].numero).toEqual(141)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
        })

        it('Deve existir documento com este número', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store: new Vuex.Store({state, mutations, actions}),
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            await flushPromises()
            const index = 0
            const documentoValidar = {id:55,idTipoDocumento:1,numero:141}
            wrapper.vm.validarNumeroUnico(documentoValidar, index)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO].mock.calls[0][1]).toEqual(48)
            expect(wrapper.vm.documentos[index].numero).toEqual(null)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Já existe um documento com esse número')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
        })

        it('Deve emitir mensagem de quantidade de cadastro de documentos excedida', async () => {
            wrapper = shallowMount(ListagemDocumentos, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors,
                    $gtag
                },
            })
            wrapper.vm.documentoVazio = true
            wrapper.vm.documentos.length = 30
            wrapper.vm.novoDocumento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('A quantidade máxima de documentos cadastrados para este patrimônio foi atingida.')
        })
    })
})
