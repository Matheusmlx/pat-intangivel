import {mount} from '@vue/test-utils'
import AzExportarRelatorioXls from './AzExportarRelatorioXls'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'

describe('AzExportarRelatorioXls', () => {
    let wrapper, localVue, actions, vuetify

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    const defaultMount = () => mount(AzExportarRelatorioXls,{
        localVue,
        vuetify,
        store: new Vuex.Store({actions})
    })

    describe('Methods', () =>{
        it('Deve gerar relatorio listagem patrimonios em XLS', async () => {
            wrapper = defaultMount()
            wrapper.vm.emitirEventoExportar()
            await flushPromises()

            expect(wrapper.emitted().exportarXls).toBeTruthy()
        })
    })
})
