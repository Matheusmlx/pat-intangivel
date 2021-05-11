import {mount, shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import ApplicationTestBuilder from '@/application/ApplicationTestBuilder'
import ContaContabilListagemTabela from './ContaContabilListagemTabela'
import {mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'
import camposPersonalizados from '@/core/store/camposPersonalizados'
import camposPersonalizadosDefault from '@/core/store/camposPersonalizados/camposPersonalizadosDefault'
jest.mock('vue-i18n')



const contaContabil = {
    id: 1,
    codigo: '12.383.55.85',
    nome: 'BENS-INTANGIVEIS-SOFTWARE',
    tipo: 'AMORTIZAVEL',
    configAmortizacao: {
        tipo: 'AMORTIZAVEL'
    },
    idConfigAmortizacao: null
}

const contaContabilEditar = {
    id: 1,
    codigo: '12.383.55.85',
    nome: 'BENS-INTANGIVEIS-SOFTWARE',
    tipo: 'AMORTIZAVEL',
    configAmortizacao: {
        tipo: 'AMORTIZAVEL'
    },
    idConfigAmortizacao: 2
}

describe('ContaContabilListagemTabela', () => {
    let wrapper, state,router, localVue, vuetify, mutations,windowSpy,$options,errors, $validator


    $validator = {
        _base: {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn(),
            },
        },
    }
    $options = {
        filters: {
            fornecedorFilter: jest.fn().mockReturnValue(true),
        }
    }

    localVue = ApplicationTestBuilder.build()

    vuetify = ApplicationTestBuilder.getVuetify()

    errors = {
        collect: jest.fn(),
    }

    router = {
        init: jest.fn(),
        push: jest.fn(),
        replace: jest.fn(),
        history: {
            current: {
                name: 'ContaContabilListagem'
            }
        }
    }


    mutations = {
        [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            }
        }



        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            location: jest.fn(),
            getComputedStyle: jest.fn(),
            addEventListener: jest.fn(),
            removeEventListener: jest.fn(),
            $i18n: {
                locale: 'camposPersonalizados',
                getLocaleMessage: () => camposPersonalizadosDefault.i18n
            }
        }))
    })

    const CamposPersonalizados = {
        namespaced: true,
        state: {
            camposPersonalizados: camposPersonalizadosDefault.i18n
        },
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getObjetoValidado: camposPersonalizados.getters.getObjetoValidado,
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
            getTooltipCamposPersonalizados: () => jest.fn()
        }
    }
    let criarStore = ({state, getters, mutations, actions}) => {
        return new Vuex.Store({state, getters, mutations, actions})
    }

    describe('Props', () => {
        it('Deve exibir a mensagem padrão quando não existir nenhuma conta contábil', () => {
            wrapper = mount(ContaContabilListagemTabela, {
                localVue,
                vuetify,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                store: new Vuex.Store({state, mutations, modules: {CamposPersonalizados}}),
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1, rowsPerPage: 10, descending: false
                    },
                    paginas: 0,
                    totalItens: 0
                },
                sync: false
            })

            const table = wrapper.find('table').findAll('tr').at(1).findAll('td')
            expect(table.at(0).text()).toEqual('Não há contas contábeis cadastradas')

        })

        it('Deve renderizar as informações na tabela', () => {
            wrapper = mount(ContaContabilListagemTabela, {
                localVue,
                vuetify,
                store: new Vuex.Store({state, mutations, modules: {CamposPersonalizados}}),
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    itens: [
                        {
                            id: 0,
                            codigo: '12.383.55.85',
                            nome: 'BENS-INTANGIVEIS-SOFTWARE',
                            tipo: 'AMORTIZAVEL',
                        }
                    ],
                    paginacao: {
                        page: 1, rowsPerPage: 10, descending: false
                    },
                    paginas: 1,
                    totalItens: 1
                },
                sync: false
            })

            const colunastabela = wrapper.find('table').findAll('tr').at(1).findAll('td')
            expect(colunastabela.length).toEqual(4)
            expect(colunastabela.at(0).text()).toEqual('12.383.55.85')
            expect(colunastabela.at(1).text()).toEqual('BENS-INTANGIVEIS-SOFTWARE')
            expect(colunastabela.at(2).text()).toEqual('Amortizável')
        })

    })

    describe('Watch', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', async() => {
            wrapper = shallowMount(ContaContabilListagemTabela, {
                store: new Vuex.Store({state, mutations, modules: {CamposPersonalizados}}),
                localVue,
                vuetify,
                mocks: {
                    $validator,
                    errors,
                    $options
                },

                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1, rowsPerPage: 10, descending: false
                    },
                    paginas: 0,
                    totalItens: 0
                }
            })

            wrapper.vm.paginacaoInterna.rowsPerPage = 20
            await flushPromises()
            expect(wrapper.emitted().paginar)
        })
    })


    describe('Events', () => {
        it('Deve emitir o evento salvar os dados de entrada com o tipo e a conta contabil', async () => {

            wrapper = shallowMount(ContaContabilListagemTabela, {
                store: new Vuex.Store({state, mutations, modules: {CamposPersonalizados}}),
                localVue,
                vuetify,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1, rowsPerPage: 10, descending: false
                    },
                    paginas: 0,
                    totalItens: 0
                }
            })
            wrapper.vm.salvaContaContabil('AMORTIZAVEL', contaContabil)
            await flushPromises()

            expect(wrapper.emitted().salvar).toBeTruthy()
            expect(wrapper.emitted().salvar[0]).toEqual([contaContabil])
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Atenção: alterar o tipo da conta contábil não afeta patrimônios ativados anteriormente!')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve emitir o evento salvar os dados de entrada com o tipo e a conta contabil', async () => {

            wrapper = shallowMount(ContaContabilListagemTabela, {
                store: new Vuex.Store({state, mutations, modules: {CamposPersonalizados}}),
                localVue,
                vuetify,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1, rowsPerPage: 10, descending: false
                    },
                    paginas: 0,
                    totalItens: 0
                }
            })
            wrapper.vm.salvaContaContabil('AMORTIZAVEL', contaContabilEditar)
            await flushPromises()

            expect(wrapper.emitted().editar).toBeTruthy()
            expect(wrapper.emitted().editar[0]).toEqual([contaContabilEditar])
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Atenção: alterar o tipo da conta contábil não afeta patrimônios ativados anteriormente!')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })
    })
})
