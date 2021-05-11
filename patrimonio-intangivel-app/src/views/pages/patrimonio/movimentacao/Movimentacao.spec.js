import {shallowMount} from '@vue/test-utils'
import Movimentacao from './Movimentacao'
import Vuex from 'vuex'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('Movimentacao.vue', () => {

    let localVue, wrapper, router, store, actions, state, mutations

    const resultado = {
        items: [],
        totalElements: 0,
        totalPages: 0
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoListagem'
                }
            }
        }

        state = {
            totalElements: 0,
            movimentacoesPendentes: {},
            buscandoPorFiltro: false,
            tabs: null,
            notificar: false,
            filtrosInterno: {},
            movimentacao: {
                todasMovimentacoes: {
                    filtros: {
                        conteudo: {
                            value: null,
                            default: null,
                            label: 'Pesquisa'
                        }
                    }
                }
            }
        }

        actions = {
            [actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACOES_PENDENTES]: jest.fn().mockReturnValue(resultado)
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO.RESETA_PAGE]: jest.fn(),
            [mutationTypes.MOVIMENTACAO.RESETA_EXTRA]: jest.fn(),
            [mutationTypes.MOVIMENTACAO.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES]: jest.fn(),
            [mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({state, actions, mutations})
    })

    describe('methods', () => {

        it('deve redirecionar para a rota MovimentacaoListagem', () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })
            flushPromises
            wrapper.vm.redirecionaTodasMovimentacoes()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoListagem'})
        })

        it('deve redirecionar para a rota MovimentacaoListagemPendente', () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })

            wrapper.vm.redirecionaMovimentacoesPendentes()

            expect(router.push.mock.calls[1][0]).toEqual({name: 'MovimentacaoListagemPendente'})
        })

        it('deve buscar as movimentações pendentes de recebimento', async () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })
            await flushPromises()
            await wrapper.vm.buscarMovimentacoesPendentes()

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACOES_PENDENTES]).toHaveBeenCalled()
            expect(wrapper.vm.totalItens).toEqual(resultado.totalElements)
        })

        it('deve validar se deve mostrar a notificação de movimentações pendentes e exibir a tela de movimentações a receber se for verdadeiro', () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })
            wrapper.setData({
                totalItens: 2
            })

            wrapper.vm.deveNotificarMovimentacaoPendente()

            expect(wrapper.vm.tabs).toEqual(1)
            expect(wrapper.vm.notificar).toEqual(true)
            expect(router.push.mock.calls[1][0]).toEqual({name: 'MovimentacaoListagemPendente'})
        })

        it('deve validar se deve mostrar a notificação de movimentações pendentes e não deve exibir a tela de movimentações a receber se for falso', () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })

            wrapper.vm.deveNotificarMovimentacaoPendente()

            expect(wrapper.vm.notificar).toEqual(false)
            expect(wrapper.vm.tabs).toEqual(0)
            expect(wrapper.findAll('v-tab-stub').at(1).text()).toContain('A Receber')
        })

        it('deve tratarEventoBuscaSimples', async () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })
            await flushPromises()
            await wrapper.vm.tratarEventoBuscaSimples('00001')

            expect(mutations[mutationTypes.MOVIMENTACAO.RESETA_PAGE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO.RESETA_EXTRA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES]).toHaveBeenCalled()
            expect(wrapper.vm.estaBuscandoPorFiltro).toEqual(true)


        })

        it('deve tratarEventoRemoverFiltro', async () => {
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })

            await wrapper.vm.tratarEventoRemoverFiltro('conteudo')
            await flushPromises()

            expect(wrapper.vm.estaBuscandoPorFiltro).toEqual(false)
        })

        it('deve tratar o evento de paginar', async () => {
            const paginacao = 2
            wrapper = shallowMount(Movimentacao, {
                localVue,
                router,
                store
            })
            await flushPromises()
            await wrapper.vm.tratarEventoPaginar(paginacao)

            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES]).toHaveBeenCalled()
        })
    })
})
