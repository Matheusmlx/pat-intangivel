import {shallowMount} from '@vue/test-utils'
import MovimentacaoDetalheLinhaDoTempo from './MovimentacaoDetalheLinhaDoTempo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('MovimentacaoDetalheLinhaDoTempo.vue', () => {

    let localVue, wrapper

    const movimentacao = {
        situacao: 'REJEITADO',
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('computed', () => {

        it('deve true quando a movimentação for rejeitada', () => {
            wrapper = shallowMount(MovimentacaoDetalheLinhaDoTempo, {
                localVue,
                propsData: {
                    movimentacao: movimentacao
                }
            })

            expect(wrapper.vm.ehMovimentacaoRejeitada).toEqual(true)
        })

        it('deve false quando a movimentação não for rejeitada', () => {
            wrapper = shallowMount(MovimentacaoDetalheLinhaDoTempo, {
                localVue,
                propsData: {
                    movimentacao: {
                        situacao: 'AGUARDANDO_ELABORACAO'
                    }
                }
            })

            expect(wrapper.vm.ehMovimentacaoRejeitada).toEqual(false)
        })

        it('deve true quando a movimentação for finalizada', () => {
            wrapper = shallowMount(MovimentacaoDetalheLinhaDoTempo, {
                localVue,
                propsData: {
                    movimentacao: {
                        situacao: 'FINALIZADO'
                    }
                }
            })

            expect(wrapper.vm.ehMovimentacaoFinalizada).toEqual(true)
        })

        it('deve false quando a movimentação não for finalizada', () => {
            wrapper = shallowMount(MovimentacaoDetalheLinhaDoTempo, {
                localVue,
                propsData: {
                    movimentacao: {
                        situacao: 'AGUARDANDO_ELABORACAO'
                    }
                }
            })

            expect(wrapper.vm.ehMovimentacaoFinalizada).toEqual(false)
        })
    })
})
