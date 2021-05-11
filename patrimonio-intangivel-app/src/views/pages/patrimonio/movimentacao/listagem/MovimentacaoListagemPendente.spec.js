import {shallowMount} from '@vue/test-utils'
import MovimentacaoListagemPendente from './MovimentacaoListagemPendente'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('MovimentacaoListagemPendente.vue', () => {

    let localVue, wrapper, router, store, state

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoListagemPendenteListagem'
                }
            }
        }

        state = {
            movimentacao: {
                movimentacaoPendente: {
                    items: [],
                    totalPages: 10,
                    totalElements: 0,
                },
                todasMovimentacoes: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 12,
                        sortDesc: [true],
                        sortBy: ['situacao']
                    }
                }
            }
        }


        store = new Vuex.Store({state})
    })

    describe('methods', () => {

        it('deve redirecionar para a rota MovimentacaoDetalhe se a movimentação estiver aguardando recebimento', () => {
            const item = {
                situacao: 'AGUARDANDO_RECEBIMENTO',
                id: 1
            }
            wrapper = shallowMount(MovimentacaoListagemPendente, {
                localVue,
                router,
                store
            })
            wrapper.vm.tratarEventoAcessar(item)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoDetalhe', params: {id: item.id}, query:{rotaName:"MovimentacaoListagemPendente"}})
        })

        it('deve tratar o evento de paginar', async () => {
            const paginacao = 2
            wrapper = shallowMount(MovimentacaoListagemPendente, {
                localVue,
                router,
                store
            })
            await flushPromises()
            await wrapper.vm.tratarEventoPaginar(paginacao)

            await flushPromises()

            expect(wrapper.emitted().tratarEventoPaginar).toBeTruthy()
            expect(wrapper.emitted().tratarEventoPaginar[0]).toEqual([paginacao])
        })
    })
})
