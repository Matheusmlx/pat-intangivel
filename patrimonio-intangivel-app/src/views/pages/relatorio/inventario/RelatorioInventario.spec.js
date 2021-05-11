import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import RelatorioInventario from './RelatorioInventario'


describe('RelatorioInventario', () => {
    let wrapper, localVue, router, state, mutations,_vm, $validator, actions, errors, vuetify, $gtag

    const orgaos = {
        items: [
            {id: 5, sigla: 'AGEREG', nome: 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande'}
        ]
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn(),
        }
        _vm = {
            $t:jest.fn()
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

        actions = {
            [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF]: jest.fn(),
            [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS]: jest.fn(),
            [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF]: jest.fn(),
            [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS]: jest.fn(),
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos)
        }

        mutations = {
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
                    name: 'inventario'
                },
            },
        }

        state = {
            relatorio: {
                inventario: {
                    filtros: {
                        tipo: {
                            value: null,
                            default: null,
                        },
                        orgao: {
                            value: null,
                            default: null,
                        },
                        mesReferencia: {
                            value: null,
                            default: null,
                        }
                    }
                }
            },
            parametros:{
                dataCorteInicioCadastroRetroativo:'08-12-2020'
            }
        }
    })

    describe('Mounted', () => {
        it('Deve buscar todos os órgãos', async () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $gtag,
                    _vm,
                    $validator,
                    errors
                },
            })
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.orgaos).toEqual(orgaos.items)
        })
    })

    describe('Computed', () => {
        it('Deve desabilitar botões de geração de relatório', () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $validator,
                    errors
                },
            })
            wrapper.vm.relatorio.orgao.value = true
            wrapper.vm.relatorio.mesReferencia.value = true
            wrapper.vm.relatorio.tipo.value = true

            expect(wrapper.vm.desabilitarBotao).toEqual(false)
        })

        it('Deve habilitar botões de geração de relatório', () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $validator,
                    errors
                },
            })
            wrapper.vm.relatorio.orgao.value = false
            wrapper.vm.relatorio.mesReferencia.value = false
            wrapper.vm.relatorio.tipo.value = false

            expect(wrapper.vm.desabilitarBotao).toEqual(true)
        })
    })

    describe('Methods', () => {
        it('Deve gerar relatorio sintetico pdf', async () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $validator,
                    errors
                },
            })
            wrapper.vm.relatorio = {
                tipo: {
                    value: 'SINTETICO'
                },
                orgao: {
                    value: 129
                },
                mesReferencia: {
                    value: '2020-04'
                },
                formato: {
                    value: 'XLS'
                }
            }

            wrapper.vm.gerarRelatorioPDF()
            await flushPromises()

            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF]).toHaveBeenCalled()
            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF].mock.calls[0][1]).toEqual(wrapper.vm.relatorio)

        })

        it('Deve gerar relatorio sintetico xls', async () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $validator,
                    errors
                },
            })

            wrapper.vm.relatorio = {
                tipo: {
                    value: 'SINTETICO'
                },
                orgao: {
                    value: 129
                },
                mesReferencia: {
                    value: '2020-04'
                },
                formato: {
                    value: 'XLS'
                }
            }

            wrapper.vm.gerarRelatorioXLS()
            await flushPromises()

            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS]).toHaveBeenCalled()
            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS].mock.calls[0][1]).toEqual(wrapper.vm.relatorio)

        })

        it('Deve gerar relatorio analítico pdf', async () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $validator,
                    errors
                },
            })

            wrapper.vm.relatorio = {
                tipo: {
                    value: 'ANALITICO'
                },
                orgao: {
                    value: 129
                },
                mesReferencia: {
                    value: '2020-04'
                },
                formato: {
                    value: 'PDF'
                }
            }

            wrapper.vm.gerarRelatorioPDF()
            await flushPromises()

            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF]).toHaveBeenCalled()
            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF].mock.calls[0][1]).toEqual(wrapper.vm.relatorio)

        })

        it('Deve gerar relatorio analítico xls', async () => {
            wrapper = shallowMount(RelatorioInventario, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                mocks: {
                    $validator,
                    errors
                },
            })

            wrapper.vm.relatorio = {
                tipo: {
                    value: 'ANALITICO'
                },
                orgao: {
                    value: 129
                },
                mesReferencia: {
                    value: '2020-04'
                },
                formato: {
                    value: 'XLS'
                }
            }

            wrapper.vm.gerarRelatorioXLS()
            await flushPromises()

            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS]).toHaveBeenCalled()
            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS].mock.calls[0][1]).toEqual(wrapper.vm.relatorio)

        })
    })
})
