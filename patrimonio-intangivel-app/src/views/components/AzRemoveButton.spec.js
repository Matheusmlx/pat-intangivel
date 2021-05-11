import {mount} from '@vue/test-utils'
import AzRemoveButton from './AzRemoveButton'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'

describe('AzRemoveButton',()=>{
    let wrapper,localVue,$validator,errors, state

    beforeEach(()=>{
        localVue = applicationTestBuilder.build()

        errors = {
            collect:jest.fn()
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    domains: [],
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        },
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Retroativo'
                        },
                    ]
                }
            }
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }
    })


    describe('Methods',()=>{
        it('Deve setar o estado do botão',()=>{
            wrapper = mount(AzRemoveButton,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                store: new Vuex.Store({
                    state
                }),
            })
            wrapper.vm.estadoBotao()

            expect(wrapper.vm.color).toEqual('red')
        })

        it('Deve mudar o estado do botão',()=>{
            wrapper = mount(AzRemoveButton,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                store: new Vuex.Store({
                    state
                }),
            })
            wrapper.vm.estadoBotao()
            wrapper.vm.estadoBotao()

            expect(wrapper.vm.color).toEqual('grey')
        })
    })

    describe('Events', () => {
        it('Deve emitir evento remover',()=>{
            wrapper = mount(AzRemoveButton,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                store: new Vuex.Store({
                    state
                }),
            })
            wrapper.vm.remove()

            expect(wrapper.emitted().remover).toBeTruthy()
        })
    })
})
