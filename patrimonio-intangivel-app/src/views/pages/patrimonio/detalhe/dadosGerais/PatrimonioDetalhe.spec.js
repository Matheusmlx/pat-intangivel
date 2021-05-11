import {shallowMount} from '@vue/test-utils'
import PatrimonioDetalhe from './PatrimonioDetalhe'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {mask} from 'vue-the-mask'

describe('PatrimonioDetalhe', () => {
    let wrapper, localVue, router, state, mutations, $validator, actions, errors, vuetify, directives, $t = jest.fn(),
        $gtag

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const defaultMount = (stubs) => shallowMount(PatrimonioDetalhe, {
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
        mocks: {$validator, errors, $t, $gtag},
    })

    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
        }
    }

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

    const patrimonioVisualizar = {
        id: 7,
        tipo: 'SOFTWARES',
        nome: 'teste',
        numero: '0000000002',
        descricao: 'edf',
        situacao: 'ATIVO',
        estado: 'PRONTO_PARA_USO',
        valorLiquido: 88.880000,
        valorAquisicao: 88.880000,
        reconhecimento: 'AQUISICAO_SEPARADA',
        dataAquisicao: null,
        dataNL: null,
        numeroNL: null,
        inicioVidaUtil: '2020-03-02T18:07:17.704+0000',
        dataVencimento: '2020-03-12T07:00:00.000+0000',
        mesesVidaUtil: 1,
        fimVidaUtil: '2020-03-12T07:00:00.000+0000',
        dataAtivacao: '2020-03-02T18:07:17.704+0000',
        fornecedor: 1,
        orgao: {
            id: 5,
            sigla: 'AGEREG',
            nome: 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande'
        },
        setor:
            {
                id: 13,
                sigla: 'DFTS',
                nome: 'Diretoria de Fiscalização e Técnicas Setoriais'
            },
        contaContabil: {
            id: 1,
            codigo: '124110100',
            descricao: 'BENS INTANGIVEIS>SOFTWARE'
        },
        configAmortizacao: {
            id: 2,
            metodo: 'QUOTAS_CONSTANTES',
            vidaUtil: 1,
            situacao: 'ATIVO',
            taxa: 100.000000,
            percentualResidual: 0.000000
        },
        periodoVidaUtil: {
            dia: 6,
            mes: 12,
        }
    }

    const fornecedorPorId = [
        {
            id: 0,
            cpfCnpj: '46456456',
            nome: 'cnpj 0',
        },
    ]

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
        totalPages: 1,
        totalElements: 4
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn(),
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }

        $gtag = {
            event: jest.fn()
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
            salvamentoAutomatico: {
                salvando: false,
            },
            patrimonio: {
                situacaoVindora: '',
            },
            rota: {
                origem: 'Inicial'
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.REVERTER_ATIVACAO]: jest.fn(),
            [actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioVisualizar),
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn(),
            [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedorPorId),
            [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos),
            [actionTypes.PATRIMONIO.BUSCAR_AMORTIZACOES]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO_BACKUP]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
            },
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
                    }
                }
            }
        }
    })

    describe('Mounted', () => {
        it('Deve buscar o patrimonio para edicao', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PATRIMONIO]).toBeDefined()
        })

        it('Não deve buscar fornecedor na api se o patrimonio não conter o id do fornecedor', async () => {
            patrimonioVisualizar.fornecedor = null
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]).not.toHaveBeenCalled()
        })
        it('Deve buscar amortizações do bem', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_AMORTIZACOES]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_AMORTIZACOES].mock.calls[0][1]).toEqual(patrimonioVisualizar.id)
        })
    })

    describe('Methods', () => {
        it('Deve reverter a ativacao do patrimonio', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoCancelarAtivacao()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.REVERTER_ATIVACAO]).toHaveBeenCalled()
        })

        it('Deve atualizar o patrimonio', async () => {
            const stub = {PatrimonioDetalheCampos: '<div><button class="stub" @click="$emit(\'salvar\')"></button></div>'}

            wrapper = defaultMount(stub)
            wrapper.find('button[class="stub"').trigger('click')
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
        })

        it('Não deve atualizar o patrimonio', async () => {
            const stub = {PatrimonioDetalheCampos: '<div><button class="stub" @click="$emit(\'salvar\')"></button></div>'}
            const mensagem = {
                message: ' O número da nota de lançamento contábil já está sendo utilizado.',
                type: 'error'
            }
            actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO] = jest.fn().mockRejectedValue(new Error('O número da nota de lançamento contábil já está sendo utilizado.'))
            router = {
                init: jest.fn(),
                push: jest.fn(),
                replace: jest.fn(),
                history: {
                    current: {
                        name: 'MenuDetalhe',
                        params: {
                            id: patrimonioVisualizar.id,
                        }
                    }
                }
            }
            wrapper = defaultMount(stub)
            wrapper.find('button[class="stub"').trigger('click')
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual(mensagem)
            expect(actions[actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID].mock.calls[0][1]).toEqual(patrimonioVisualizar.id)
            expect(wrapper.vm.$data.patrimonio).toEqual(patrimonioVisualizar)
        })
    })
})
