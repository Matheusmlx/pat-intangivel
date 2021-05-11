import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoNovaMovimentacao from './BotaoNovaMovimentacao'

describe('BotaoNovaMovimentacao.vue', () => {

    let localVue, wrapper

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {

        it('deve emitir o evento de cadastrarMovimentacao para o componente pai', async () => {
            wrapper = shallowMount(BotaoNovaMovimentacao, {
                localVue
            })
            await wrapper.vm.cadastrarMovimentacao()

            expect(wrapper.emitted().cadastrarMovimentacao).toBeTruthy()
            expect(wrapper.emitted().cadastrarMovimentacao[0]).toEqual([wrapper.vm.$data.tipo])
        })
    })
})
