import {shallowMount} from '@vue/test-utils'
import MovimentacaoDetalheAbaDocumentos
    from '@/views/pages/patrimonio/movimentacao/visualizacao/MovimentacaoDetalheAbaDocumentos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'
import ListagemDocumentos from "@/views/pages/patrimonio/edicao/documentos/ListagemDocumentos";

describe('MovimentacaoDetalheAbaDocumentos', () => {
    let wrapper, localVue, store, state, mutations, $validator, errors, actions, router, vuetify, directives,
        $t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const patrimonio = {
        id: 1
    }

    const defaultMount = (stubs) => shallowMount(MovimentacaoDetalheAbaDocumentos, {
        localVue,
        directives,
        router,
        vuetify,
        stubs,
        propsData: {patrimonio: patrimonio},
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                CamposPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
    })

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
        ]
    }


    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
        }
    }

    const documentos = [{numero: 141, data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'uriAnexo'},
        {numero: 142, data: '', valor: '', idTipoDocumento: 2, uriAnexo: 'uriAnexo'},
        {numero: 143, data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'uriAnexo'},
        {numero: 144, data: '', valor: '', idTipoDocumento: 3, uriAnexo: 'uriAnexo'}]

    const dado = {
        numero: '141',
        data: ' ',
        movimentacaoId: 48,
        valor: '698',
        idPatrimonio: 1,
        idTipoDocumento: 2,
        uriAnexo: 'notafiscal.pdf'
    }

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
            movimentacao: {
                documentos: documentos
            },
            tiposDocumento: [],
            documentos: [{id: 1, numero: 141, data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'uriAnexo'}]
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
                    name: 'MovimentacaoDetalhe',
                    params: {
                        id: 1,
                    },
                },
            },
        }

        actions = {
            [actionTypes.MOVIMENTACAO.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.DOCUMENTO.DOCUMENTO_UPLOAD_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO]: jest.fn().mockReturnValue(tipoDocumentos.items),
            [actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO]: jest.fn().mockReturnValue(documentos),
            [actionTypes.MOVIMENTACAO.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            ['SET_UPLOADED_FILES']: jest.fn(),
            [mutationTypes.MOVIMENTACAO.DOCUMENTO.REMOVER_DOCUMENTOS_MOVIMENTACAOES]: jest.fn(),
            [mutationTypes.MOVIMENTACAO.SET_DOCUMENTOS]: jest.fn()
        }

        store = new Vuex.Store({mutations, state, actions})
    })

    describe('Methods', () => {
        it('Deve cadastrar um Documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvar(dado)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO]).toHaveBeenCalled()
        })

        it('Deve adicionar um novo Documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.docNovo = {}
            wrapper.vm.novoDocumento()
            await flushPromises

            expect(wrapper.vm.adicionando).toEqual(true)
            expect(wrapper.vm.docNovo.idMovimentacao).toEqual(wrapper.vm.$route.params.id)
        })

        it('Deve buscar os documentos', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.buscarDocumentos()

            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO]).toHaveBeenCalled()
        })

        it('Deve salvar o anexo', async () => {
            const anexoRetorno = {uriAnexo: 'uriAnexo', idTipoDocumento: 1}
            wrapper = defaultMount()
            wrapper.vm.tiposDocumento = tipoDocumentos
            await flushPromises
            await wrapper.vm.tratarEventoSalvarAnexo(anexoRetorno)
            await flushPromises

            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.DOCUMENTO_UPLOAD_MOVIMENTACAO]).toHaveBeenCalled()
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

        it('Deve Finalizar o cadastro para realizar a edição', async () => {
            wrapper = defaultMount()
            wrapper.vm.estaAdicionando()
            await flushPromises

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para realizar a edição')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve Finalizar a edição do documento antes de criar um novo', async () => {
            wrapper = defaultMount()
            wrapper.vm.setaEditando('Numero')
            wrapper.vm.novoDocumento()
            await flushPromises

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize a edição do documento antes de criar um novo')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve mostrar um alert caso o numero do documento seja igual a outro do mesmo tipo', async () => {
            wrapper = defaultMount()
            const documento = {id: 1, numero: 141, data: '', valor: '', idTipoDocumento: 1, uriAnexo: 'uriAnexo'}

            wrapper.vm.validarNumeroUnico(documento)
            await flushPromises()
            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO].mock.calls[0][1]).toEqual(1)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Já existe um documento com esse número')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
        })

        it('Deve excluir um documento', async () => {
            wrapper = defaultMount()
            const documento = {id: 1, numero: '23232', idPatrimonio: 1, idMovimentacao: 2, idTipoDocumento: 2}
            wrapper.vm.removerDocumento(documento)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO]).toHaveBeenCalled()
        })

        it('Deve deletar um DocumentoNovo', async () => {
            wrapper = defaultMount()

            wrapper.vm.tratarEventoDeletarDocumentoNovo()
            await flushPromises()

            expect(wrapper.vm.adicionando).toEqual(false)
            expect(wrapper.vm.docNovo).toEqual({})
        })
    })
})
