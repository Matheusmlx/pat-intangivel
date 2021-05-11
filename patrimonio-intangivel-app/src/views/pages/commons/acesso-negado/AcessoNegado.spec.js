import {shallowMount} from '@vue/test-utils'
import AcessoNegado from './AcessoNegado'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('AcessoNegado.vue', () => {

    let localVue, wrapper

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('renderização', () => {

        it('deve renderizar uma mensagem de acesso negado ', () => {
            wrapper = shallowMount(AcessoNegado, {
                localVue
            })

            expect(wrapper.findAll('v-col-stub').at(0).text()).toContain('thumb_down_alt')
            expect(wrapper.findAll('v-col-stub').at(1).text()).toContain('Você não possui permissão para acessar este sistema!')
        })
    })
})
