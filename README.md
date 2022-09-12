# Student grade web service

## Requests and authorization

|                                      | Administrator | Teacher | Student |
|:-------------------------------------|:--------------|:--------|:--------|
| `POST api/auth/signup`               | +             | +       | +       |
| `POST api/auth/changepass`           | -             | +       | +       | 
| `GET api/empl/payment`               | -             | +       | +       | 
| `POST api/acct/payments`             | -             | -       | +       | 
| `PUT api/acct/payments`              | -             | -       | +       |
| `GET api/admin/user`                 | -             | -       | -       |
| `DELETE api/admin/user`              | -             | -       | -       | 
| `PUT api/admin/user/role`            | -             | -       | -       |
| `PUT api/admin/user/access`          | -             | -       | -       |
| `POST api/admin/breached-password`   | -             | -       | -       |
| `DELETE api/admin/breached-password` | -             | -       | -       |
| `GET api/security/events`            | -             | -       | -       |