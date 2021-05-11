import {shallowMount} from '@vue/test-utils'
import AzInfoButton from './AzInfoButton'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('AzInfoButton.vue', () => {

    let localVue, wrapper

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('renderização', () => {

        it('deve mostrar o menu com as opções DADOS GERAIS e MOVIMENTAÇÕES para alterna entre as telas', () => {
            wrapper = shallowMount(AzInfoButton, {
                localVue
            })

            expect(wrapper.find('span[class="az-info"]').exists()).toBeTruthy()
            expect(wrapper.find('v-menu-stub').exists()).toBeTruthy()
            expect(wrapper.find('div[class="az-info-content"]').exists()).toBeTruthy()
        })
    })
})
