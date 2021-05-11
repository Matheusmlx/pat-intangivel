import {shallowMount} from '@vue/test-utils'
import MovimentacaoListagem from './MovimentacaoListagem'
import Vuex from 'vuex'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('MovimentacaoListagem.vue', () => {

    let localVue, wrapper, router, store, actions, mutations, state, $gtag

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
                    name: 'MovimentacaoListagemListagem'
                }
            }
        }

        $gtag = {
            event: jest.fn()
        }

        state = {
            movimentacao: {
                todasMovimentacoes: {
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: null
                        }
                    },
                    paginacao: {
                        page: 1,
                        rowsPerPage: 12,
                        sortDesc: [true],
                        sortBy: ['situacao']
                    }
                }
            }
        }

        actions = {
            [actionTypes.MOVIMENTACAO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(resultado)
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES]: jest.fn(),
            [mutationTypes.MOVIMENTACAO.RESETA_EXTRA]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    describe('watch', () => {
        it('deve buscar as movimentações quando os filtros sofre alteração', async () => {
            wrapper = shallowMount(MovimentacaoListagem, {
                localVue,
                router,
                store,
                mocks: {
                    $gtag
                }
            })
            await flushPromises
            state.movimentacao.todasMovimentacoes.filtros = {conteudo: {value: 'teste'}}

            await flushPromises
            expect(mutations[mutationTypes.MOVIMENTACAO.RESETA_EXTRA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_TODAS_MOVIMENTACOES]).toHaveBeenCalled()
        })
    })

    describe('methods', () => {

        it('deve redirecionar para a rota NovaMovimentacao se a movimentação estiver em elaboração', () => {
            const item = {
                situacao: 'EM_ELABORACAO',
                patrimonio: 1
            }
            wrapper = shallowMount(MovimentacaoListagem, {
                localVue,
                router,
                store,
                mocks: {
                    $gtag
                }
            })
            wrapper.vm.tratarEventoAcessar(item)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'NovaMovimentacao', params: {id: item.patrimonio}, query: {rotaName: "MovimentacaoListagem"}})
        })

        it('deve redirecionar para a rota MovimentacaoDetalhe se a movimentação estiver finalizada ou aguardando recebimento', () => {
            const item = {
                situacao: 'FINALIZADO',
                id: 1
            }
            wrapper = shallowMount(MovimentacaoListagem, {
                localVue,
                router,
                store,
                mocks: {
                    $gtag
                }
            })
            wrapper.vm.tratarEventoAcessar(item)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoDetalhe', params: {id: item.id}, query: {rotaName: "MovimentacaoListagem"}})
        })

        it('deve buscar todas as movimentações', async () => {
            wrapper = shallowMount(MovimentacaoListagem, {
                localVue,
                router,
                store,
                mocks: {
                    $gtag
                }
            })
            await flushPromises()
            await wrapper.vm.buscarTodasMovimentacoes()

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_TODAS_MOVIMENTACOES]).toHaveBeenCalled()
            expect(wrapper.vm.itens).toEqual(resultado.items)
            expect(wrapper.vm.paginas).toEqual(resultado.totalPages)
            expect(wrapper.vm.totalItens).toEqual(resultado.totalElements)
        })

        it('deve tratar o evento de paginar', async () => {
            const paginacao = 2
            wrapper = shallowMount(MovimentacaoListagem, {
                localVue,
                router,
                store,
                mocks: {
                    $gtag
                }
            })
            await flushPromises()
            await wrapper.vm.tratarEventoPaginar(paginacao)

            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES]).toHaveBeenCalled()
        })

        it('deve returnar uma cópia dos filtros de todas movimentações', () => {
            wrapper = shallowMount(MovimentacaoListagem, {
                localVue,
                router,
                store,
                mocks: {
                    $gtag
                }
            })

            wrapper.vm.getFiltros()
            expect(wrapper.vm.getFiltros()).toEqual(state.movimentacao.todasMovimentacoes.filtros)
        })
    })
})
