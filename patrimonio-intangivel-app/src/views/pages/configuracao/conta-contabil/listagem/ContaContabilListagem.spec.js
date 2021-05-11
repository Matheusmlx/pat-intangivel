import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import ApplicationTestBuilder from '@/application/ApplicationTestBuilder'
import ContaContabilListagem from './ContaContabilListagem'

let criarStore = ({state, getters, mutations, actions}) => {
    return new Vuex.Store({state, getters, mutations, actions})
}

describe('ContaContabilListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, vuetify

    const contaContabil = {
        id: 1,
        codigo: '12.383.55.85',
        nome: 'BENS-INTANGIVEIS-SOFTWARE',
        tipo: 'AMORTIZAVEL',
        configAmortizacao: {
            tipo: 'AMORTIZAVEL'
        }
    }

    const data = {
        itens: [
            {
                id: 0,
                codigo: '12.383.55.85',
                nome: 'BENS-INTANGIVEIS-SOFTWARE',
                tipo: 'AMORTIZAVEL',
                configAmortizacao: {
                    tipo: 'AMORTIZAVEL'
                }
            },
        ],
        totalPages: 1,
        totalElements: 1,
    }

    beforeEach(() => {
        localVue = ApplicationTestBuilder.build()
        vuetify = ApplicationTestBuilder.getVuetify()
        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: {
                name: 'ContaContabilListagem'
                }},
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
            rota: {
                origem: 'Inicial'
            },
            contaContabil: {
                todasContasContabeis: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                        sortBy: [''],
                    },
                    filtros: {
                        conteudo: {
                            value: null,
                            default: null,
                            label: 'Pesquisa',
                        },
                    },
                },
            },
        }

        mutations = {
            [mutationTypes.CONTA_CONTABIL
                .SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS]: jest.fn(),
            [mutationTypes.CONTA_CONTABIL
                .SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS]: jest.fn(),
            [mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
        }

        actions = {
            [actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]: jest.fn().mockReturnValue(data),
            [actionTypes.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL]: jest.fn(),
            [actionTypes.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL]: jest.fn()
        }
    })

    describe('Events', () => {
        it('Deve emitir evento paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(ContaContabilListagem, {
                localVue,
                vuetify,
                router,
                store: criarStore({state, mutations, actions}),
            })

            wrapper.vm.tratarEventoPaginar({})

            await flushPromises()

            expect(
                mutations[
                    mutationTypes.CONTA_CONTABIL
                        .SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS
                    ].mock.calls[0][1]
            ).toEqual({})
            expect(
                mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock
                    .calls[0][1]
            ).toBe('Carregando ...')
            expect(
                actions[
                    actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS
                    ]
            ).toHaveBeenCalled()
        })

        it('Deve emitir o evento de buscarlistagem simples', async () => {
            wrapper = shallowMount(ContaContabilListagem, {
                localVue,
                vuetify,
                router,
                store: criarStore({state, mutations, actions}),
            })

            wrapper.vm.tratarEventoBuscaSimples('Aquisição')

            await flushPromises()

            expect(
                mutations[
                    mutationTypes.CONTA_CONTABIL
                        .SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS
                    ].mock.calls[0][1].conteudo.value
            ).toEqual('Aquisição')
            expect(
                mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock
                    .calls[0][1]
            ).toBe('Carregando ...')
            expect(
                actions[
                    actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS
                    ]
            ).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(
                'Aquisição'
            )
        })
    })

    describe('Methods', () => {
        it('Deve emitir o evento salvar os dados de entrada da conta contábil com sucesso', async () => {

            wrapper = shallowMount(ContaContabilListagem, {
                store: new Vuex.Store({state, mutations, actions}),
                router,
                localVue,
                vuetify,
            })

            wrapper.vm.tratarEventoSalvar(contaContabil)
            await flushPromises()
            expect(actions[actionTypes.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL].mock.calls[0][1]).toEqual(contaContabil)
            expect(actions[actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]).toHaveBeenCalled()
        })

        it('Deve editar os dados da conta contábil', async () => {

            wrapper = shallowMount(ContaContabilListagem, {
                store: new Vuex.Store({state, mutations, actions}),
                router,
                localVue,
                vuetify,
            })

            wrapper.vm.tratarEventoEditar(contaContabil)
            await flushPromises()
            expect(actions[actionTypes.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL].mock.calls[0][1]).toEqual(contaContabil)
            expect(actions[actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]).toHaveBeenCalled()
        })

        it('Deve remover filtros internos', () => {

            wrapper = shallowMount(ContaContabilListagem, {
                store: new Vuex.Store({state, mutations, actions}),
                router,
                localVue,
                vuetify,
            })

            const propriedade = 1
            wrapper.vm.filtrosInterno[propriedade] = {
                value: 1,
                default: 2
            }
            wrapper.vm.tratarEventoRemoverFiltro(propriedade)

            expect(wrapper.vm.filtrosInterno[propriedade].value).toEqual(wrapper.vm.filtrosInterno[propriedade].default)
            expect(actions[actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]).toHaveBeenCalled()
        })
    })
})
