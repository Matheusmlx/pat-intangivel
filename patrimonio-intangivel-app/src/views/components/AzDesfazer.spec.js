import {mount} from '@vue/test-utils'
import AzDesfazer from '@/views/components/AzDesfazer'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

describe('AzDesfaer', () => {
    let wrapper, localVue, vuetify, state

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    beforeEach(async () => {
        state = {
            show: true
        }
    })

    const defaultMount = () => mount(AzDesfazer, {
        localVue,
        vuetify,
        store: new Vuex.Store({state})
    })

    describe('Emmited', () => {
        it('Deve emitir evento desfazer passando true como parametro ', async () => {

            wrapper = defaultMount()

            wrapper.vm.undoAction()
            await flushPromises()

            expect(wrapper.emitted('desfazer', true)).toBeTruthy()
            expect(wrapper.emitted('desfazer', [0])).toEqual([[true]])
        })

        it('Deve emitir o evento closeButtom e setar para false a propriedade show', async () => {
            wrapper = defaultMount()

            wrapper.vm.closeShow()
            await flushPromises()

            expect(wrapper.vm.show).toEqual(false)
            expect(wrapper.emitted('closeButtom')).toBeTruthy()
        })
    })
})
