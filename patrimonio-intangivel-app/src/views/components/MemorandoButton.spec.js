import {mount} from '@vue/test-utils'
import MemorandoButton from './MemorandoButton'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes} from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'

describe('MemorandoButton', () => {
    let wrapper, localVue, actions, vuetify

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    beforeEach(() => {
        actions = {
            [actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_PDF]: jest.fn()
        }
    })

    const defaultMount = () => mount(MemorandoButton,{
        localVue,
        vuetify,
        store: new Vuex.Store({actions}),
        propsData:{
            patrimonioId: 2
        },
    })

    describe('Methods', () =>{
        it('Deve gerar relatorio memorando pdf', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoGerarMemorando()
            await flushPromises()

            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_PDF]).toHaveBeenCalled()
            expect(actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_PDF].mock.calls[0][1]).toEqual(2)
        })
    })
})
