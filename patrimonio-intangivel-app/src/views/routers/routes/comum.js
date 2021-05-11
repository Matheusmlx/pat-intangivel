import RedirecionaEditarUsuario from '@/views/pages/commons/perfil/RedirecionaEditarUsuario'
import PaginaNaoEncontrada from '@/views/pages/commons/pagina-nao-encontrada/PaginaNaoEncontrada'
import Manualize from '@/views/pages/manualize/Manualize'
import AcessoNegado from '@/views/pages/commons/acesso-negado/AcessoNegado'

export default [
    {
        path: '/perfil',
        name: 'RedirecionarEditarUsuario',
        component: RedirecionaEditarUsuario,
    },
    {
        path: '*',
        name: 'PaginaNaoEncontrada',
        component: PaginaNaoEncontrada,
        meta: {
            page: {
                title: 'Ooops',
                subtitle: 'Página não encontrada'
            }
        }
    },
    {
        path: '/manualize',
        name: 'Manualize',
        component: Manualize,
        meta: {
            menu: {
                title: 'Manuais',
                icon: 'fas fa-lightbulb'
            },
            page: {
                title: 'Manuais',
                subtitle: 'Lista de manuais do sistema'
            },
        requiresAuth: true,
        authorities: ['Patrimonio.Consulta']
        }
    },
    {
        path: '*',
        name: 'AcessoNegado',
        component: AcessoNegado,
        meta: {
            page: {
                title: 'Acesso Negado',
            }
        }
    },
]
