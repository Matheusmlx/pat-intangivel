import Relatorio from '@/views/pages/relatorio/Relatorio'
import Inventario from '@/views/pages/relatorio/inventario/RelatorioInventario.vue'

export default [
    {
        path: '/relatorio',
        name: 'Relatorios',
        component: Relatorio,
        meta:{
            menu:{
                title:'Relatórios ',
                icon: 'fas fa-file-download'
            },
            requiresAuth: true,
            authorities: ['Patrimonio.Consulta']
        },
        children: [
            {
                path: '/inventario',
                name: 'inventario',
                component:Inventario,
                meta:{
                    menu:{
                        title: 'Inventário'
                    },
                    page:{
                        title: 'Relatórios',
                        subtitle: 'Inventário'
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Consulta']
                }
            }
        ]
    }

]
