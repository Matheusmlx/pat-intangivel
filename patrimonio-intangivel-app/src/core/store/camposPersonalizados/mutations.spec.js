import mutations from './mutations'
import getters from './getters'
import camposPersonalizadosDefault from "./camposPersonalizadosDefault";

jest.mock('vue-i18n')

describe('mutations', () => {
    let windowSpy

    beforeEach(() => {
        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            $i18n: {
                locale: 'camposPersonalizados',
                getLocaleMessage: () => camposPersonalizadosDefault.i18n
            }
        }))
    })

    const state = {
        camposPersonalizados: {}
    }

    it('deve setar um novo camposPersonalizados', () => {
        mutations.SET_ROTULOS_PERSONALIZADOS(state, camposPersonalizadosDefault.i18n)
        expect(getters.getCamposPersonalizados()).toEqual(camposPersonalizadosDefault.i18n)
    })
})
